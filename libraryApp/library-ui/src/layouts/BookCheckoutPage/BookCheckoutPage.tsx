import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { REACT_API_URL, renderBookImage, renderDefaultBookImage, requestOptions } from "../Utils/AppUtil";
import { StarsReview } from "../Utils/StarsReview";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";
import ReviewModel from "../../models/ReviewModel";
import { LatestReviews } from "./LatestReviews";
import { useOktaAuth } from "@okta/okta-react";
import ReviewRequestModel from "../../models/ReviewRequestModel";

export const BookCheckoutPage = () => {

	const { authState } = useOktaAuth();

	const [book, setBook] = useState<BookModel>();

	const [isLoading, setIsLoading] = useState(true);

	const [httpError, setHttpError] = useState(null);

	const [reviews, setReviews] = useState<ReviewModel[]>([]);

	const [totalStars, setTotalStars] = useState(0);

	const [isLoadingReviews, setIsLoadingReviews] = useState(true);

	const [currentLoansCount, setCurrentLoansCount] = useState(0);

	const [isLoadingCurrentLoansCount, setIsLoadingCurrentLoansCount] = useState(true);

	const [isCheckedOut, setIsCheckedOut] = useState(false);

	const [isLoadingBookCheckedOut, setIsLoadingBookCheckedOut] = useState(true);

	const [isReviewLeft, setIsReviewLeft] = useState(false);

	const [isLoadingUserReview, setIsLoadingUserReview] = useState(true);

	const [displayError, setDisplayError] = useState(false);

	const bookId = (window.location.pathname).split('/')[2];

	useEffect(() => {
		const fetchBook = async () => {
			const baseUrl: string = `${REACT_API_URL}/books/${bookId}`;
			const response = await fetch(baseUrl);
			if (!response.ok) {
				throw new Error('Something went wrong!');
			}
			const responseJson = await response.json();
			const loadedBook: BookModel = {
				id: responseJson.id,
				title: responseJson.title,
				author: responseJson.author,
				description: responseJson.description,
				copies: responseJson.copies,
				copiesAvailable: responseJson.copiesAvailable,
				category: responseJson.category,
				image: responseJson.image,
			};
			setBook(loadedBook);
			setIsLoading(false);
		};
		fetchBook().catch((error) => {
			setIsLoading(false);
			setHttpError(error.message);
		});
	}, [bookId, isCheckedOut]);

	useEffect(() => {
		const fetchBookReviews = async () => {
			const reviewUrl: string = `${REACT_API_URL}/reviews/search/findByBookId?bookId=${bookId}`;
			const responseReviews = await fetch(reviewUrl);
			if (!responseReviews.ok) {
				throw new Error('Something went wrong!');
			}
			const responseJsonReviews = await responseReviews.json();
			const responseData = responseJsonReviews._embedded.reviews;
			const loadedReviews: ReviewModel[] = []; 
			let weightedStarReviews: number = 0;
			for (const key in responseData) {
				loadedReviews.push({
					id: responseData[key].id,
					userEmail: responseData[key].userEmail,
					date: responseData[key].date,
					rating: responseData[key].rating,
					bookId: responseData[key].bookId,
					reviewDescription: responseData[key].reviewDescription
				});
				weightedStarReviews += responseData[key].rating;
			}
			if (loadedReviews) {
				const round = (Math.round((weightedStarReviews / loadedReviews.length) * 2) / 2 ).toFixed(1);
				setTotalStars(Number(round));
			}
			setReviews(loadedReviews);
			setIsLoadingReviews(false);
		}
		fetchBookReviews().catch((error: any) => {
			setIsLoadingReviews(false);
			setHttpError(error.message);
		});
	}, [bookId, isReviewLeft]);

	useEffect(() => {
		const fetchUserCurrentLoansCount = async () => {
			if (authState && authState.isAuthenticated) {
				const url = `${REACT_API_URL}/books/secure/currentloans/count`;
				const currentLoansCountResponse = await fetch(url, requestOptions('GET', `Bearer ${authState.accessToken?.accessToken}`));
				if (!currentLoansCountResponse.ok) {
					throw new Error('Something went wrong!');
				}
				const currentLoansCountResponseJson = await currentLoansCountResponse.json();
				setCurrentLoansCount(currentLoansCountResponseJson);
			}
			setIsLoadingCurrentLoansCount(false);
		}
		fetchUserCurrentLoansCount().catch((error: any) => {
			setIsLoadingCurrentLoansCount(false);
			setHttpError(error.message);
		});
	}, [authState, isCheckedOut]);

	useEffect(() => {
		const fetchUserCheckedOutBook = async () => {
			if (authState && authState.isAuthenticated) {
				const url = `${REACT_API_URL}/books/secure/ischeckedout/byuser?bookId=${bookId}`;
				const bookCheckedOut = await fetch(url, requestOptions('GET', `Bearer ${authState.accessToken?.accessToken}`));
				if (!bookCheckedOut.ok) {
					throw new Error('Something went wrong!');
				}
				const bookCheckedOutResponseJson = await bookCheckedOut.json();
				setIsCheckedOut(bookCheckedOutResponseJson);
			}
			setIsLoadingBookCheckedOut(false);
		}
		fetchUserCheckedOutBook().catch((error: any) => {
			setIsLoadingBookCheckedOut(false);
			setHttpError(error.message);
		})
	}, [authState, bookId]);

	useEffect(() => {
		const fetchUserReviewBook = async () => {
			if (authState && authState.isAuthenticated) {
				const url = `${REACT_API_URL}/reviews/secure/user/book?bookId=${bookId}`;
				const userReview = await fetch(url, requestOptions('GET', `Bearer ${authState.accessToken?.accessToken}`));
				if (!userReview.ok) {
					throw new Error('Something went wrong');
				}
				const userReviewResponseJson = await userReview.json();
				setIsReviewLeft(userReviewResponseJson);
			}
			setIsLoadingUserReview(false);
		}
		fetchUserReviewBook().catch((error: any) => {
			setIsLoadingUserReview(false);
			setHttpError(error.message);
		})
	}, [authState, bookId]);

	if (isLoading || isLoadingReviews || isLoadingCurrentLoansCount || isLoadingBookCheckedOut || isLoadingUserReview) {
		return (
			<SpinnerLoading />
		);
	};
	
	if (httpError) {
		<div className="container m-5">
			<p>{httpError}</p>
		</div>
	};

	async function checkoutBook() {
		const url = `${REACT_API_URL}/books/secure/checkout?bookId=${book?.id}`;
		/*const requestOptions = {
			method: 'PUT',
			headers: {
				Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
				'Content-Type': 'application/json'
			}
		};*/
		const checkoutResponse = await fetch(url, requestOptions('PUT', `Bearer ${authState?.accessToken?.accessToken}`));
		if (!checkoutResponse.ok) {
			setDisplayError(true);
			throw new Error('Something went wrong!');
		}
		setDisplayError(false);
		setIsCheckedOut(true);
	};

	async function submitReview(starInput: number, reviewDescription: string) {
		let bookId: number = 0;
		if (book?.id) {
			bookId = book.id;
		}
		const reviewRequestModel = new ReviewRequestModel(starInput, bookId, reviewDescription);
		const url = `${REACT_API_URL}/reviews/secure`;
		const bearerToken = `Bearer ${authState?.accessToken?.accessToken}`;
		const requestBody = JSON.stringify(reviewRequestModel);
		/*const requestOptions = {
			method: 'POST',
			headers: {
				Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(reviewRequestModel)
		};*/
		const returnResponse = await fetch(url, requestOptions('POST', bearerToken, requestBody));
		if (!returnResponse.ok) {
			throw new Error('Something went wrong!');
		}
		setIsReviewLeft(true);
	}

	const defaultBookImage = '../../Images/BooksImages/book-luv2code-1000.png';

	return (
		<div>
			<div className="container d-none d-lg-block">
				{displayError && 
					<div className="alert alert-danger mt-3" role="alert">
						Please pay outstanding fees and/or return late book(s).
					</div>
				}
				<div className="row mt-5">
					<div className="col-sm-2 col-md-2">
						{book?.image ? 
							renderBookImage(book.image, '226', '349', 'Book') 
							: 
							renderDefaultBookImage(defaultBookImage, '226', '349', 'Book')
						}
					</div>
					<div className="col-4 col-md-4 container">
						<div className="ml-2">
							<h2>{book?.title}</h2>
							<h5 className="text-primary">{book?.author}</h5>
							<p className="lead">{book?.description}</p>
							<StarsReview rating={totalStars} size={32} />
						</div>
					</div>
					<CheckoutAndReviewBox 
						book={book} 
						mobile={false} 
						currentLoansCount={currentLoansCount} 
						isAuthenticated={authState?.isAuthenticated} 
						isCheckedOut={isCheckedOut} 
						checkoutBook={checkoutBook}
						isReviewLeft={isReviewLeft}
						submitReview={submitReview}
					/>
				</div>
				<hr />
				<LatestReviews reviews={reviews} bookId={book?.id} mobile={false} />
			</div>
			{/* Mobile */}
			<div className="container d-lg-none mt-5">
				<div className="d-flex justify-content-center align-items-center">
					{book?.image ? 
						renderBookImage(book.image, '226', '349', 'Book') 
						: 
						renderDefaultBookImage(defaultBookImage, '226', '349', 'Book')
					}
				</div>
				<div className="mt-4">
					<div className="ml-2">
						<h2>{book?.title}</h2>
						<h5 className="text-primary">{book?.author}</h5>
						<p className="lead">{book?.description}</p>
						<StarsReview rating={totalStars} size={32} />
					</div>
				</div>
				<CheckoutAndReviewBox 
					book={book} 
					mobile={true} 
					currentLoansCount={currentLoansCount} 
					isAuthenticated={authState?.isAuthenticated} 
					isCheckedOut={isCheckedOut}
					checkoutBook={checkoutBook}
					isReviewLeft={isReviewLeft}
					submitReview={submitReview}
				/>
				<hr />
				<LatestReviews reviews={reviews} bookId={book?.id} mobile={true} />
			</div>
		</div>
	);

}
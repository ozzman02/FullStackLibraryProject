import { useEffect, useState } from "react";
import { ReturnBook } from "./ReturnBook";
import BookModel from "../../../models/BookModel";
import { SpinnerLoading } from "../../Utils/SpinnerLoading";
import { Link } from "react-router-dom";
import { REACT_API_URL } from "../../Utils/AppUtil";


export const Carousel = () => {

	const [books, setBooks] = useState<BookModel[]>([]);

	const [isLoading, setIsLoading] = useState(true);

	const [httpError, setHttpError] = useState(null);

	useEffect(() => {
		
		const fetchBooks = async () => {
			const url: string = `${REACT_API_URL}/books?page=0&size=9`;
			console.log(url);
			const response = await fetch(url);
			if (!response.ok) {
				throw new Error('Something went wrong!');
			}
			const responseJson = await response.json();
			const responseData = responseJson._embedded.books;
			const loadedBooks: BookModel[] = [];
			for (const key in responseData) {
				loadedBooks.push({
					id: responseData[key].id,
					title: responseData[key].title,
					author: responseData[key].author,
					description: responseData[key].description,
					copies: responseData[key].copies,
					copiesAvailable: responseData[key].copiesAvailable,
					category: responseData[key].category,
					image: responseData[key].image,
				});
			}
			setBooks(loadedBooks);
			setIsLoading(false);
		};
		fetchBooks().catch((error) => {
			setIsLoading(false);
			setHttpError(error.message);
		});
	}, []);

	if (isLoading) {
		return (
			<SpinnerLoading />
		);
	};

	if (httpError) {
		<div className="container m-5">
			<p>{httpError}</p>
		</div>
	};

	const getBooksFromList = (beginIndex: number, endIndex: number) => {
		return books.slice(beginIndex, endIndex).map(book => (
			<ReturnBook book={book} key={book.id} />
		));
	};

	return (
    	<div className="container mt-5" style={{ height: 550 }}>
      		<div className="homepage-carousel-title">
        		<h3>Find your next "I stayed up too late reading" book.</h3>
      		</div>
      		<div
        		id="carouselExampleControls"
        		className="carousel carousel-dark slide mt-5 d-none d-lg-block"
        		data-bs-interval="false"
      		>
        		{/* Desktop */}
        		<div className="carousel-inner">
          			<div className="carousel-item active">
            			<div className="row d-flex justify-content-center align-items-center">
              				{getBooksFromList(0, 3)}
            			</div>
          			</div>
          			<div className="carousel-item">
            			<div className="row d-flex justify-content-center align-items-center">
							{getBooksFromList(3, 6)}
            			</div>
          			</div>
          			<div className="carousel-item">
            			<div className="row d-flex justify-content-center align-items-center">
							{getBooksFromList(6, 9)}
            			</div>
          			</div>
        		</div>
        		<button
          			className="carousel-control-prev"
          			type="button"
          			data-bs-target="#carouselExampleControls"
          			data-bs-slide="prev"
        		>
          			<span className="carousel-control-prev-icon" aria-hidden="true"></span>
          			<span className="visually-hidden">Previous</span>
        		</button>
        		<button
          			className="carousel-control-next"
          			type="button"
          			data-bs-target="#carouselExampleControls"
          			data-bs-slide="next"
        		>
          			<span className="carousel-control-next-icon" aria-hidden="true"></span>
          			<span className="visually-hidden">Next</span>
        		</button>
      		</div>
      		{/* Mobile */}
      		<div className="d-lg-none mt-3">
        		<div className="row d-flex justify-content-center align-items-center">
					<ReturnBook book={books[7]} key={books[7].id}/>
        		</div>
      		</div>
      		<div className="homepage-carousel-title mt-3">
        		<Link className="btn btn-outline-secondary btn-lg" to='/search'>View More</Link>
      		</div>
    	</div>
	);
};

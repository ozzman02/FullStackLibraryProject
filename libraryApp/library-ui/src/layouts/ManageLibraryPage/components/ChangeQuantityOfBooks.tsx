import { Fragment, useEffect, useState } from "react";
import BookModel from "../../../models/BookModel";
import { SpinnerLoading } from "../../Utils/SpinnerLoading";
import { Pagination } from "../../Utils/Pagination";
import { ChangeQuantityOfBook } from "./ChangeQuantityOfBook";
import { REACT_API_URL } from "../../Utils/AppUtil";

export const ChangeQuantityOfBooks = () => {

	const [books, setBooks] = useState<BookModel[]>([]);

	const [isLoading, setIsLoading] = useState(true);

	const [httpError, setHttpError] = useState(null);

	const [currentPage, setCurrentPage] = useState(1);

	const [booksPerPage] = useState(5);

	const [totalAmountOfBooks, setTotalAmountOfBooks] = useState(0);

	const [totalPages, setTotalPages] = useState(0);

	const [bookDelete, setBookDelete] = useState(false);

	useEffect(() => {
		const fetchBooks = async () => {
			const baseUrl: string = `${REACT_API_URL}/books?page=${currentPage - 1}&size=${booksPerPage}`;
			const response = await fetch(baseUrl);
			if (!response.ok) {
				throw new Error('Something went wrong!');
			}
			const responseJson = await response.json();
			const responseData = responseJson._embedded.books;
			setTotalAmountOfBooks(responseJson.page.totalElements);
			setTotalPages(responseJson.page.totalPages);
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
	}, [currentPage, booksPerPage, bookDelete]);

	const deleteBook = () => setBookDelete(!bookDelete);

	const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

	const indexOfLastBook: number = currentPage * booksPerPage;
	
	const indexOfFirstBook: number = indexOfLastBook - booksPerPage;

	let lastItem = booksPerPage * currentPage <= totalAmountOfBooks ? booksPerPage * currentPage : totalAmountOfBooks;

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
	
	return (
		<div className="container mt-5">
			{totalAmountOfBooks > 0 ?
				<Fragment>
					<div className="mt-3"><h3>Number of results: ({totalAmountOfBooks})</h3></div>
					<p>{indexOfFirstBook + 1} to {lastItem} of {totalAmountOfBooks}</p>
					{books.map(book => (
						<ChangeQuantityOfBook book={book} key={book.id} deleteBook={deleteBook}/>
					))}
				</Fragment>
				:
				<h5>Add a book before changing quantity</h5>
			}
			{totalPages > 1 && <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate} />}
		</div>
	);
}
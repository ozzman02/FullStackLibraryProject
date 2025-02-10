import { useEffect, useState } from "react";
import BookModel from "../../../models/BookModel";
import { renderBookImage, renderDefaultBookImage } from "../../Utils/AppUtil";

export const ChangeQuantityOfBook: React.FC<{ 
	book: BookModel 
}> = (props, key) => {
	
	const [quantity, setQuantity] = useState<number>(0);

	const [remaining, setRemaining] = useState<number>(0);

	useEffect(() => {
		const fetchBooksInState = () => {
			props.book.copies ? setQuantity(props.book.copies) : setQuantity(0);
			props.book.copiesAvailable ? setRemaining(props.book.copiesAvailable) : setRemaining(0);
		};
		fetchBooksInState();
	}, []);

	const defaultBookImage = './../../../Images/BooksImages/book-luv2code-1000.png';

	return (
		<div className="card mt-3 shadow p-3 mb-3 bg-body rounded">
			<div className="row g-0">
				<div className="col-md-2">
					<div className="d-none d-lg-block">
						{props.book.image ?
							renderBookImage(props.book.image, '123', '196', 'Book') 
							: 
							renderDefaultBookImage(defaultBookImage, '123', '196', 'Book')
						}
					</div>
					<div className="d-lg-none d-flex justify-content-center align-items-center">
						{props.book.image ?
							renderBookImage(props.book.image, '123', '196', 'Book') 
							: 
							renderDefaultBookImage(defaultBookImage, '123', '196', 'Book')
						}
					</div>
				</div>
				<div className="col-md-6">
					<div className="card-body">
						<h5 className="card-title">{props.book.author}</h5>
						<h4>{props.book.title}</h4>
						<p className="card-text">{props.book.description}</p>
					</div>
				</div>
				<div className="mt-3 col-md-4">
					<div className="d-flex justify-content-center align-items-center">
						<p>Total Quantity: <b>{quantity}</b></p>
					</div>
					<div className="d-flex justify-content-center align-items-center">
						<p>Books Remaining: <b>{remaining}</b></p>
					</div>
				</div>
				<div className="mt-3 col-md-1">
					<div className="d-flex justify-content-start">
						<button className="m-1 btn btn-md btn-danger">Delete</button>
					</div>
				</div>
				<button className="m1 btn btn-md main-color text-white">Add Quantity</button>
				<button className="m1 btn btn-md btn-warning">Decrease Quantity</button>
			</div>
		</div>
	);
}
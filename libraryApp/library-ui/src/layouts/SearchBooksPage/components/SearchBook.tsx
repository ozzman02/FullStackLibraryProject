import { Link } from "react-router-dom";
import BookModel from "../../../models/BookModel";
import { renderBookImage, renderDefaultBookImage } from "../../Utils/AppUtil";

export const SearchBook: React.FC<{ book: BookModel }> = (props) => {

	const defaultBookImage = '../../Images/BooksImages/book-luv2code-1000.png';

	return (
		<div className="card mt-3 shadow p-3 mb-3 bg-body rounded">
			<div className="row g-0">
				<div className="col-md-2">
					<div className="d-none d-lg-block">
						{props.book.image ? 
							renderBookImage(props.book.image, "123", "196", "book") 
							: 
							renderDefaultBookImage(defaultBookImage, "123", "196", "Book")
						}
					</div>
					<div className="d-lg-none d-flex justify-content-center align-items-center">
						{props.book.image ? 
							renderBookImage(props.book.image, "123", "196", "book") 
							: 
							renderDefaultBookImage(defaultBookImage, "123", "196", "Book")
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
				<div className="col-md-4 d-flex justify-content-center align-items-center">
					<Link className="btn btn-md main-color text-white" to={`/checkout/${props.book.id}`}>View Details</Link>
				</div>
			</div>
		</div>
	);
}
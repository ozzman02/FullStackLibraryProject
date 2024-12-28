import BookModel from "../../../models/BookModel";
import { renderBookImage, renderDefaultBookImage } from "../../Utils/AppUtil";

export const ReturnBook: React.FC<{book: BookModel}> = (props) => {
	
	return (
		<div className="col-xs-6 col-sm-6 col-md-4 col-lg-3 mb-3">
    		<div className="text-center">
				{props.book.img ? renderBookImage(props.book.img, "151", "233", "book") : renderDefaultBookImage("151", "233", "book")}
                <h6 className="mt-2">{props.book.title}</h6>
                <p>{props.book.author}</p>
                <a className="btn main-color text-white" href="#">Reserve</a>
            </div>
        </div>
	);
}
import { Link } from "react-router-dom";

export const renderDefaultBookImage = (width: string, height: string, alt: string) => {
	return (
		<img src={require("../../Images/BooksImages/book-luv2code-1000.png")} width={width} height={height} alt={alt} />
	);
};

export const renderBookImage = (img: string, width: string, height: string, alt: string) => {
	return (
		<img src={img} width={width} height={height} alt={alt} />
	);
};

export const showExploreTopBooksBtn = () => {
	return (
		<Link className="btn main-color btn-lg text-white" to="search">Explore top books</Link>
	);
};

export const showSignUpBtn = () => {
	return (
		<Link className="btn main-color btn-lg text-white" to="/login">Sign up</Link>
	);
};

export const showLibraryServicesBtn = () => {
	return (
		<Link className="btn main-color btn-lg text-white" to="/messages">Library Services</Link>
	);
};
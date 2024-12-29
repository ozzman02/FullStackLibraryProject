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
import { Fragment } from "react/jsx-runtime";
import { Carousel } from "./Components/Carousel";
import { ExploreTopBooks } from "./Components/ExploreTopBooks";
import { Heroes } from "./Components/Heroes";
import { LibraryServices } from "./Components/LibraryServices";

export const HomePage = () => {
	return (
		<Fragment>
			<ExploreTopBooks />
			<Carousel />
			<Heroes />
			<LibraryServices />
		</Fragment>
	);
}
import { useOktaAuth } from "@okta/okta-react";
import { Fragment, useEffect, useState } from "react";
import MessageModel from "../../../models/MessageModel";
import { SpinnerLoading } from "../../Utils/SpinnerLoading";
import { Pagination } from "../../Utils/Pagination";

export const AdminMessages = () => {

	const { authState } = useOktaAuth();

	const [isLoadingMessages, setIsLoadingMessages] = useState(true);
	const [httpError, setHttpError] = useState(null);
	const [messages, setMessages] = useState<MessageModel[]>([]);
	const [messagesPerPage] = useState(5);
	const [currentPage, setCurrentPage] = useState(1);
	const [totalPages, setTotalPages] = useState(0);

	useEffect(() => {
		const fetchUserMessages = async () => {
			if (authState && authState?.isAuthenticated) {
				const baseUrl = "http://localhost:8080/api/messages/search/findByClosed";
				const url = `${baseUrl}?closed=false&page=${currentPage - 1}&size=${messagesPerPage}`;
				/*const requestOptions = {
					method: 'GET',
					headers: {
						Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
						'Content-Type': 'application/json'
					}
				};*/
				const messagesResponse = await fetch(url);
				if (!messagesResponse.ok) {
					throw new Error('Something went wrong!');
				}
				const messagesResponseJson = await messagesResponse.json();
				const responseData = messagesResponseJson._embedded.messages;
				setTotalPages(messagesResponseJson.page.totalPages);
				const loadedMessages: MessageModel[] = [];
				for (const key in responseData) {
					loadedMessages.push({
						title: responseData[key].title,
						question: responseData[key].question,
						id: responseData[key].id,
						userEmail: responseData[key].userEmail,
						adminEmail: responseData[key].adminEmail,
						response: responseData[key].response,
						closed: responseData[key].closed
					});
				}
				setMessages(loadedMessages);
			}
			setIsLoadingMessages(false);
		}
		fetchUserMessages().catch((error: any) => {
			setIsLoadingMessages(false);
			setHttpError(error.message);
		})
		window.scrollTo(0, 0);
	}, [authState, currentPage, messagesPerPage]);

	if (isLoadingMessages) {
		return (<SpinnerLoading/>);
	}

	if (httpError) {
		return (<div className="container m-5"><p>{httpError}</p></div>);
	}

	const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

	return (
		<div className="mt-3">
			{messages.length > 0 ? 
				<Fragment>
					<h5>Pending Q/A: </h5>
					{messages.map(message => (
						<p>Questions that need a response</p>
					))}
				</Fragment> 
				:
				<h5>No pending Q/A</h5>
			}
			{totalPages > 1 && <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate} />}
		</div>	
	);
}
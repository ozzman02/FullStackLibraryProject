import "./App.css";
import TodoTable from "./components/TodoTable";

function App() {

	const todos = [
		{ rowNumber: 1, rowDescription: 'Feed puppy', rowAssigned: 'User One' },
		{ rowNumber: 2, rowDescription: 'Water plants', rowAssigned: 'User Two' },
		{ rowNumber: 3, rowDescription: 'Make dinner', rowAssigned: 'User Three' },
		{ rowNumber: 4, rowDescription: 'Charge Phone Battery', rowAssigned: 'User Four' }
	]

	return (
		<div className="mt-5 container">
			<div className="card">
        		<div className="card-header">Your Todo's</div>
				<div className="card-body">
					<TodoTable todos={todos} />
        		</div>
			</div>
		</div>
  	);
}

export default App;

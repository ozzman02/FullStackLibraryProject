import { useState } from "react";
import "./App.css";
import { TodoTable } from "./components/TodoTable";
import { NewTodoForm } from "./components/NewTodoForm";

export const App = () => {

	const [showAddTodoForm, setShowAddTodoForm] = useState(false);

	const [todos, setTodos] = useState([
		{ rowNumber: 1, rowDescription: 'Feed puppy', rowAssigned: 'User One' },
		{ rowNumber: 2, rowDescription: 'Water plants', rowAssigned: 'User Two' },
		{ rowNumber: 3, rowDescription: 'Make dinner', rowAssigned: 'User Three' },
		{ rowNumber: 4, rowDescription: 'Charge Phone Battery', rowAssigned: 'User Four' }
	]);

	const addTodo = (description: string, assigned: string) => {
		let rowNumber = 0;
		if (todos.length > 0) {
			rowNumber = todos[todos.length - 1].rowNumber + 1;
		} else {
			rowNumber = 1;
		}
		if (description !== undefined && assigned !== undefined) {
			const newTodo = {rowNumber: rowNumber, rowDescription: description, rowAssigned: assigned };
			setTodos(todos => [...todos, newTodo]);
		}
	};

	const deleteTodo = (deleteTodoRowNumber: number) => {
		// creating a new array
		let filtered = todos.filter(function (value) {
			return value.rowNumber !== deleteTodoRowNumber;
		});
		setTodos(filtered);
	}

	return (
		<div className="mt-5 container">
			<div className="card">
        		<div className="card-header">Your Todo's</div>
				<div className="card-body">
					<TodoTable todos={todos} deleteTodo={deleteTodo} />
					<button 
						className="btn btn-primary" 
						onClick={() => setShowAddTodoForm(!showAddTodoForm)}
					>
						{showAddTodoForm ? 'Close New Todo' : 'Add new todo'}
					</button>
					{showAddTodoForm && (<NewTodoForm addTodo={addTodo} />)}
        		</div>
			</div>
		</div>
  	);
}

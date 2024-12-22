import { useState } from "react";

function NewTodoForm(props) {

    const [description, setDescription] = useState('');

    const [assigned, setAssigned] = useState('');

    /*const descriptionChange = (event) => {
        setDescription(event.target.value);
    }

    const assignedChange = (event) => {
        setAssigned(event.target.value);
    }*/

    const onChange = (e) => {
        const { name, value } = e.target;
        if (name === 'assigned') setAssigned(value);
        if (name === 'description') setDescription(value);
    }

    const submitTodo = () => {
        if (description !== '' && assigned !== '') {
            props.addTodo(description, assigned);
            setAssigned('');
            setDescription('');
        }
    }

    return (
        <div className="mt-5">
            <form>
                <div className="mb-3">
                    <label className="form-label">Assigned</label>
                    <input 
                        name="assigned"
                        type="text" 
                        value={assigned} 
                        className="form-control" 
                        required 
                        onChange={onChange}
                        // onChange={e => setAssigned(e.target.value)}
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Description</label>
                    <textarea
                        name="description" 
                        className="form-control" 
                        rows={3} 
                        required 
                        onChange={onChange}
                        // onChange={e => setDescription(e.target.value)}
                        value={description}
                    />
                </div>
                <button 
                    type="button" 
                    className="btn btn-primary mt-3" 
                    onClick={submitTodo}
                >
                    Add Todo
                </button>
            </form>
        </div>
    )
}

export default NewTodoForm;
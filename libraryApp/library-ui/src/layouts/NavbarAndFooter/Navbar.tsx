import { Link, NavLink } from "react-router-dom";
import { useOktaAuth } from "@okta/okta-react";
import { SpinnerLoading } from "../Utils/SpinnerLoading";

export const Navbar = () => {

	const { oktaAuth, authState } = useOktaAuth();

	if (!authState) {
		return <SpinnerLoading />
	}

	const handleLogout = async () => oktaAuth.signOut();
	
	/*const handleLogout = () => {
		oktaAuth.closeSession();	
		oktaAuth.clearStorage();
		setTimeout(() => {
			window.location.href = '/';
		}, 500);
	}*/

	const showSignInButton = () => {
		return (
			<li className='nav-item m-1'>
            	<Link type='button' className='btn btn-outline-light' to='/login'>Sign in</Link>
            </li>
		);
	};

	const showLogoutButton = () => {
		return (
			<li>
				<button className="btn btn-outline-light" onClick={handleLogout}>Logout</button>
			</li>
		);
	}

	/** Auth Token */
	console.log(authState);

    return (
        <nav className='navbar navbar-expand-lg navbar-dark main-color py-3'>
            <div className='container-fluid'>
                <span className='navbar-brand'>Luv 2 Read</span>
                <button 
                    className='navbar-toggler' 
                    type='button' 
                    data-bs-toggle='collapse' 
                    data-bs-target='#navbarNavDropdown'
                    aria-controls='navbarNavDropdown'
                    aria-expanded='false'
                    aria-label='Toggle Navigation'
                >
                    <span className='navbar-toggler-icon'></span>
                </button>
                <div className='collapse navbar-collapse' id='navbarNavDropdown'>
                    <ul className='navbar-nav'>
                        <li className='nav-item'>
                            <NavLink className='nav-link' to="/home">Home</NavLink>
                        </li>
                        <li className='nav-item'>
                            <NavLink className='nav-link' to="/search">Search Books</NavLink>
                        </li>
						{authState.isAuthenticated &&
							<li className='nav-item'>
                            	<NavLink className='nav-link' to="/shelf">Shelf</NavLink>
                        	</li>
						}
						{authState.isAuthenticated &&
							<li className='nav-item'>
                            	<NavLink className='nav-link' to="/fees">Pay Fees</NavLink>
                        	</li>
						}
						{authState.isAuthenticated && authState.accessToken?.claims?.userType === 'admin' && 
							<li className='nav-item'>
                            	<NavLink className='nav-link' to="/admin">Admin</NavLink>
                        	</li>
						}

                    </ul>
                    <ul className='navbar-nav ms-auto'>
						{!authState.isAuthenticated ? showSignInButton() : showLogoutButton()}
                    </ul>
                </div>
            </div>
        </nav>
    );
}
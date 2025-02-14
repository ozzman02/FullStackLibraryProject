export const renderDefaultBookImage = (path: string, width: string, height: string, alt: string) => {
	return (
		<img src={require(path)} width={width} height={height} alt={alt} />
	);
};

export const renderBookImage = (img: string, width: string, height: string, alt: string) => {
	return (
		<img src={img} width={width} height={height} alt={alt} />
	);
};

export const REACT_API_URL = process.env.REACT_APP_API?.slice(0);

export const requestOptions = (method: string, bearerToken: string, body?: any) => {
	if (body) {
		return {
			method: method,
			headers: {
				Authorization: bearerToken,
				'Content-Type': 'application/json'
			}	
		}
	} else {
		return {
			method: method,
			headers: {
				Authorization: bearerToken,
				'Content-Type': 'application/json'
			}
		}
	}
};

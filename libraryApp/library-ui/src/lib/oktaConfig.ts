export const oktaConfig = {
	clientId: '0oam7ngaa292k1b3h5d7',
	issuer: 'https://dev-85267849.okta.com/oauth2/default',
	redirectUri: 'http://localhost:3000/login/callback',
	scopes: ['openid', 'profile', 'email'],
	pkce: true,
	disableHttpsCheck: true
}
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { App}  from './App';
import { BrowserRouter } from 'react-router-dom';
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';

const stripePromise = loadStripe('pk_test_51Qs7XTD1NRMN0DZaBOJq85SDcK1Qvmyg0OLocHuCyMMrzI6PwTTaX8PPQx5FcCwqjM4zIOEX9AfJIAtnrjxMjEnC00NPxyfKcX');

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <BrowserRouter>
	<Elements stripe={stripePromise}>
		<App />
	</Elements>
  </BrowserRouter>
);

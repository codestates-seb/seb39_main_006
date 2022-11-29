import styled from "styled-components";

export const SubmitBtn = styled.button`
	display: inline-block;
	margin: 0.5rem auto;
	font-size: 1.25rem;
	background-color: #8fc9e0;
	width: 87%;
	border: none;
	padding: 0.7rem 1.2rem;
	margin: 0.5rem;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
	border-radius: 5px;
	font-weight: 600;
	color: white;
	cursor: pointer;
	&:hover {
		background-color: #5e98ae;
	}
	&:disabled {
		border: 1px solid #999999;
		background-color: #cccccc;
		color: #666666;
		pointer-events: none;
	}
`;
// export const SignupBtn = styled(SubmitBtn);
export const ValidateBtn = styled.button`
	margin: 0.5rem auto;
	padding: 10px 0;
	border: none;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
	border-radius: 5px;
	font-weight: 600;
	color: white;
	background-color: #8fc9e0;
	&:hover {
		background-color: #5e98ae;
	}
`;
const StyledBtn = styled.button`
	display: grid;
	grid: auto / repeat(auto-fit, minmax(200px, auto));
	place-items: center;
	font-size: 1.25rem;
	background-color: #cfbcb7;
	width: fit-content;
	border: 1px solid #cfbcb7;
	padding: 0.5rem 1rem;
	margin: 0.5rem;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
	color: #425049;
	&:hover {
		background-color: #d7e1dc;
		border-color: #d7e1dc;
	}
	&:disabled {
		border: 1px solid #999999;
		background-color: #cccccc;
		color: #666666;
		pointer-events: none;
	}
`;

export default StyledBtn;

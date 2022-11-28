import React from "react";
import Posts from "../postpage/Posts";
import styled from "styled-components";
import MainSubHeader from "./MainSubHeader";

const MainPage = () => {
	return (
		<>
			<Flex>
				<Section>
					<section>
						<h1>Main Page</h1>
						<h2>여행동행자 모집합니다</h2>
					</section>
				</Section>
				<MainSubHeader />
				<Posts />
			</Flex>
		</>
	);
};

export default MainPage;

const Flex = styled.div`
	display: flex;
	flex-direction: column;
	z-index: -1;
`;
const Section = styled.div`
	margin: auto;
	section {
		display: flex;
		position: relative;
		list-style: none;
	}

	h1 {
		font-size: 52px;
		display: block;
		font-family: "Montserrat", sans-serif;
		text-transform: uppercase;
		text-shadow: 1px 1px 0px #cfbcb7, 2px 2px 0px #cfbcb7, 3px 3px 0px #cfbcb7,
			4px 4px 0px #cfbcb7;
	}
	h2 {
		padding: 1rem;
		font-size: 15px;
		display: block;
		font-family: "Montserrat", sans-serif;
		text-transform: uppercase;
		text-shadow: 1px 1px 0px #cfbcb7, 1px 1px 0px #cfbcb7, 2px 2px 0px #cfbcb7,
			3px 3px 0px #cfbcb7;
	}
`;

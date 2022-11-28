import React from "react";
import styled from "styled-components";

const Footer = () => {
	return (
		<FooterSection>
			<ul className="footerInfo">
				<li>
					<a
						href="https://github.com/jyj2187/hitch_hiker"
						target="_blank"
						rel="noreferrer">
						Github Repository
					</a>
				</li>
				<li>
					<a
						href="https://github.com/jyj2187/hitch_hiker/wiki"
						target="_blank"
						rel="noreferrer">
						프로젝트 Wiki
					</a>
				</li>
				<li>
					<a href="https://github.com/ki-hong" target="_blank" rel="noreferrer">
						김기홍
					</a>
				</li>
				<li>
					<a
						href="https://github.com/bizbaeja"
						target="_blank"
						rel="noreferrer">
						배자현
					</a>
				</li>
				<li>
					<a href="https://github.com/Lmoti" target="_blank" rel="noreferrer">
						이동기
					</a>
				</li>
				<li>
					<a href="https://github.com/jyj2187" target="_blank" rel="noreferrer">
						정윤조
					</a>
				</li>
			</ul>
			<div className="footerLogo">
				<p data-item="HITCH : HIKER">HITCH : HIKER</p>
			</div>
		</FooterSection>
	);
};

export default Footer;

const FooterSection = styled.div`
	display: flex;
	flex-direction: column;
	background: white;
	opacity: 0.75;
	margin: 1rem 0 0;
	padding: 1.2rem 0 3rem;

	a {
		cursor: pointer;
		text-decoration: none;
		color: black;
	}

	.footerInfo {
		width: 90%;
		max-width: 1080px;
		display: flex;
		justify-content: space-around;
		margin: 0 auto;
		flex-direction: row;
	}

	.footerLogo {
		border-top: 1px solid #ececec;
		padding-top: 1.5rem;
		margin: 0 auto;
		display: inline-block;

		// 왜 안됨?
		p {
			cursor: default;
			font-size: 32px;
			color: #003046;
			text-transform: uppercase;
			font-weight: 400;
			transition: all 1s ease-in-out;
			position: relative;

			&::before {
				content: attr(data-item);
				transition: all 1s ease-in-out;
				white-space: pre;
				color: #e5e5e5;
				position: absolute;
				top: 0;
				bottom: 0;
				left: 0;
				right: 0;
				width: 0;
				overflow: hidden;
			}

			&:hove {
				&::before {
					width: 100%;
				}
			}
		}
	}
`;

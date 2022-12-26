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

const FooterSection = styled.footer`
	display: flex;
	flex-direction: column;
	background: rgba(171, 217, 255, 0.1);
	margin: 0 auto;
	padding-top: 1.5rem;
	width: 100%;
	height: 8rem;
	position: absolute;
	transform: translateY(-100%);
	z-index: 1;

	a {
		cursor: pointer;
		font-weight: 600;
		text-decoration: none;
		color: black;
	}

	.footerInfo {
		width: 90%;
		max-width: 900px;
		display: flex;
		justify-content: space-between;
		margin: 0 auto;
		flex-direction: row;
		border-bottom: 1px solid #333333;
		padding-bottom: 1.5rem;
	}

	.footerLogo {
		margin: 1.5rem auto;
		display: flex;
		align-items: center;
		justify-content: center;

		// 왜 안됨?
		p {
			cursor: default;
			font-size: 48px;
			color: #003046;
			white-space: pre;
			text-align: center;
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

import React from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
const SideBar = () => {
	const navigate = useNavigate();

	const mypageHandler = () => {
		navigate("/mypage");
	};
	const mybookmarkHandler = () => {
		navigate("/mybookmark");
	};
	const mypostkHandler = () => {
		navigate("/mypost");
	};
	const mymatchinfokHandler = () => {
		navigate("/mymatchinfo");
	};
	const messagesHandler = () => {
		navigate("/messages");
	};
	const myChatHandler = () => {
		navigate("/chat");
	};
	return (
		<SideBars>
			<nav>
				<ul>
					<button onClick={mypageHandler}>
						<li id="home">
							<div className="home-icon">
								<div className="roof">
									<div className="roof-edge"></div>
								</div>
								<div className="front"></div>
							</div>
						</li>
					</button>
					<button onClick={mybookmarkHandler}>
						<li id="about">
							<div className="about-icon">
								<div className="head">
									<div className="eyes"></div>
									<div className="beard"></div>
								</div>
							</div>
						</li>
					</button>
					<button onClick={mypostkHandler}>
						<li id="work">
							<div className="work-icon">
								<div className="paper"></div>
								<div className="lines"></div>
								<div className="lines"></div>
								<div className="lines"></div>
							</div>
						</li>
					</button>
					<button onClick={mymatchinfokHandler}>
						<li id="mail">
							<div className="mail-icon">
								<div className="mail-base">
									<div className="mail-top"></div>
								</div>
							</div>
						</li>
					</button>
					<button onClick={messagesHandler}>
						<li id="newmail">
							<div className="newmail-icon">
								<div className="newmail-base">
									<div className="newmail-top"></div>
								</div>
							</div>
						</li>
					</button>
					<button onClick={myChatHandler}>
						<li id="newmail">
							<div className="newmail-icon">
								<div className="newmail-base">
									<div className="newmail-top"></div>
								</div>
							</div>
						</li>
					</button>
				</ul>
			</nav>
		</SideBars>
	);
};

export default SideBar;

const SideBars = styled.div`
	*,
	*:before,
	*:after {
		box-sizing: border-box;
	}
	button {
		display: block;
		background-color: none;
		color: none;
		border: none;
	}
	:after {
		content: "";
		list-style: none;
	}

	section {
		position: relative;
		left: 100px;
		list-style: none;
	}

	h1 {
		margin: 80px 0 10px 0;
		font-size: 52px;
		font-family: "Montserrat", sans-serif;
		text-transform: uppercase;
		text-shadow: 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9, 3px 3px 0px #dabbc9,
			4px 4px 0px #dabbc9;
	}

	h2 {
		font-size: 24px;
	}

	body {
		padding: 100px 0;
		background: lightblue;
		color: white;
		max-width: 640px;
		font-family: "Montserrat", sans-serif;
		font-size: 14px;
		line-height: 1.4;
	}
	a {
		width: 50px;
		color: inherit;
		text-decoration: none;
	}
	nav {
		margin-left: 2.2rem;
		margin-top: 2rem;
		float: left;
		position: relative;
		top: 0;
		left: 0;
		background: transparent;
	}

	nav ul {
		text-align: center;
		list-style: none;
		margin-left: 2rem;
	}

	nav ul li {
		position: relative;
		margin-bottom: 10px;
		width: 70px;
		cursor: pointer;
		background: #cfaeba;
		text-transform: uppercase;
		transition: all 0.4s ease-out;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.9);
		border-radius: 10px;
	}

	nav ul li:after {
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.9);
		border-radius: 10px;
		position: absolute;
		background: white;
		color: #cfaeba;
		top: 0;
		left: 70px;
		width: 70px;
		height: 100%;
		opacity: 0.5;
		transform: perspective(400px) rotateY(90deg);
		transform-origin: 0 100%;
		transition: all 0.4s ease-out;
	}

	#home:after {
		content: "UserInfo";
		line-height: 88px;
		font-size: 12px;
	}
	#about:after {
		content: "북마크";
		line-height: 88px;
	}
	#work:after {
		content: "내 게시글";
		line-height: 85px;
	}
	#mail:after {
		content: "매칭현황";
		line-height: 70px;
	}
	#newmail:after {
		content: "알림들";
		line-height: 70px;
	}
	nav ul li:hover {
		transform: translateX(-70px);
	}

	nav ul li:hover:after {
		opacity: 1;
		transform: perspective(400px) rotateY(0deg) scale(1);
	}

	nav ul li > div {
		display: inline-block;
		padding: 25px 0;
		background: transparent;
	}

	nav ul li div {
		position: relative;
	}

	.roof {
		width: 0;
		height: 0;
		top: 2px;
		border-style: solid;
		border-width: 0 21px 15px 21px;
		border-color: transparent transparent #ffffff transparent;
	}

	.roof-edge {
		position: absolute;
		z-index: 20;
		left: -17px;
		top: 3px;
		width: 0;
		height: 0;
		border-style: solid;
		border-width: 0 17px 12px 17px;
		border-color: transparent transparent #425049 transparent;
	}
	/*white triangle over red triangle*/
	.roof-edge:after {
		position: absolute;
		left: -14.5px;
		top: 3px;
		width: 0;
		height: 0;
		border-style: solid;
		border-width: 0 14.5px 10px 14.5px;
		border-color: transparent transparent white transparent;
	}

	.front {
		position: relative;
		top: 3px;
		width: 28.5px;
		height: 20px;
		margin: 0 auto;
		background: white;
	}
	/*door*/
	.front:after {
		position: absolute;
		background: #425049;
		width: 11px;
		height: 13px;
		bottom: 0;
		left: 9px;
	}

	/*/// About me ////*/

	.head {
		width: 32px;
		height: 35px;
		background: white;
		border-radius: 8px;
	}
	/*mouth*/
	.head:after {
		width: 4px;
		height: 10px;
		background: white;
		position: absolute;
		border-radius: 4px 0 0 4px;
		top: 21px;
		left: 14px;
		transform: rotate(270deg);
	}

	.eyes {
		width: 8px;
		height: 5px;
		border-radius: 50%;
		position: absolute;
		top: 10px;
		left: 5px;
		background: #425049;
	}
	/*right eye*/
	.eyes:after {
		width: 8px;
		height: 5px;
		border-radius: 50%;
		position: absolute;
		top: 0;
		left: 14px;
		background: #425049;
	}

	.beard {
		width: 32px;
		height: 17px;
		background: #425049;
		border: 2px solid white;
		position: absolute;
		bottom: 0;
		left: 0;
		border-radius: 0 0 8px 8px;
	}
	/*nose*/
	.beard:after {
		position: absolute;
		top: -2px;
		left: 11px;
		background: white;
		width: 6px;
		height: 4px;
		border-left: 1px solid #425049;
		border-right: 1px solid #425049;
	}

	/*//work//*/

	.paper {
		position: relative;
		height: 32px;
		width: 29px;
		background: white;
		border: 2px solid white;
	}

	/*window*/
	.paper:after {
		position: absolute;
		top: 1px;
		left: 0;
		width: 25px;
		height: 29px;
		background: white;
		border-top: 4px solid #425049;
	}

	.lines {
		position: absolute;
		top: 36px;
		left: 5px;
		width: 11px;
		box-shadow: 0 0 0 1px #425049;
	}

	.lines:after {
		position: absolute;
		top: 4px;
		left: 3px;
		width: 14px;
		box-shadow: 0 0 0 1px #425049;
	}

	.lines:nth-child(2) {
		position: absolute;
		top: 44px;
		left: 8px;
		width: 11px;
	}

	.lines:nth-child(2):after {
		position: absolute;
		top: 4px;
		left: -3px;
		width: 11px;
	}

	.lines:nth-child(3) {
		position: absolute;
		top: 52px;
		left: 8px;
		width: 14px;
	}

	.lines:nth-child(3):after {
		display: none;
	}

	/*//mail //*/

	.mail-base {
		position: relative;
		width: 32px;
		height: 18px;
		background: white;
	}

	.mail-top {
		position: absolute;
		z-index: 20;
		left: 0;
		top: 0;
		width: 0;
		height: 0;
		transform: rotate(180deg);
		border-style: solid;
		border-width: 0 16px 11px 16px;
		border-color: transparent transparent #425049 transparent;
	}

	.mail-top:after {
		position: absolute;
		z-index: 20;
		left: -16px;
		top: 3px;
		width: 0;
		height: 0;
		border-style: solid;
		border-width: 0 16px 9px 16px;
		border-color: transparent transparent white transparent;
	}
	.newmail-top {
		width: 8px;
		height: 5px;
		border-radius: 50%;
		position: absolute;
		top: 10px;
		left: 5px;
		background: #425049;
	}
	.newmail-base {
		position: relative;
		width: 32px;
		height: 18px;
		background: white;
	}

	.newmail-top:after {
		position: absolute;
		z-index: 20;
		left: -16px;
		top: 3px;
		width: 0;
		height: 0;
		border-style: solid;
		border-width: 0 16px 9px 16px;
		border-color: transparent transparent white transparent;
	}
`;

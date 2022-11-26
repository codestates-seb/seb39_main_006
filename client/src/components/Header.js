import React, { useEffect, useRef, useState } from "react";
import imgLogo from "../img/realWave.gif";
import profileImg from "../img/bell.png";
import { useNavigate } from "react-router-dom";
import styled, { css } from "styled-components";
import SockJs from "sockjs-client";
import StompJs from "stompjs";
import axios from "axios";

const Header = () => {
	const [msgs, setMsgs] = useState([]);
	const [msgIds, setMsgIds] = useState([]);
	const [showMsg, setShowMsg] = useState(false);
	const navigate = useNavigate();
	const ref = useRef(null);

	const logoutHandler = () => {
		axios(`${process.env.REACT_APP_URL}/api/members/logout`, {
			headers: {
				method: "Post",
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		});
		sessionStorage.clear();
		navigate(`/`);
		window.location.reload();
	};

	useEffect(() => {
		if (sessionStorage.getItem("isLogin")) {
			axios(`${process.env.REACT_APP_URL}/api/messages/not-read`, {
				headers: {
					access_hh: sessionStorage.getItem("AccessToken"),
				},
			})
				.then((res) => {
					setMsgs((msgs) => [...msgs, ...res.data.data]);
					res.data.data.map((el) => {
						return setMsgIds((msgIds) => [...msgIds, el.messageId]);
					});
				})
				.catch((err) => {
					if (err.response.status === 400) {
						if (err.response.data.fieldErrors) {
							alert(err.response.data.fieldErrors[0].reason);
						} else if (
							err.response.data.fieldErrors === null &&
							err.response.data.violationErrors
						) {
							alert(err.response.data.violationErrors[0].reason);
						} else {
							alert(
								"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
							);
						}
					} else if (err.response.status === 0)
						alert(
							"서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
						);
					else {
						if (
							err.response.data.korMessage ===
							"만료된 토큰입니다. 다시 로그인 해주세요."
						) {
							sessionStorage.clear();
							navigate(`/`);
							window.location.reload();
						} else if (err.response.data.korMessage) {
							alert(err.response.data.korMessage);
						} else {
							alert(
								"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
							);
						}
					}
					window.location.reload();
				});
		}
	}, []);

	useEffect(() => {
		if (sessionStorage.getItem("isLogin")) {
			const socket = new SockJs(`${process.env.REACT_APP_URL}/websocket`);
			const client = StompJs.over(socket);
			client.debug = null;
			client.connect(
				{
					access_hh: sessionStorage.getItem("AccessToken"),
				},
				() => {
					client.subscribe(
						"/topic/" + sessionStorage.getItem("memberId"),
						(msg) => {
							setMsgs((msgs) => [...msgs, JSON.parse(msg.body)]);
							setMsgIds((msgIds) => [
								...msgIds,
								JSON.parse(msg.body).messageId,
							]);
						}
					);
				}
			);
		}
	}, []);

	const msgClickHandler = (msgId, postId) => {
		axios(`${process.env.REACT_APP_URL}/api/messages/read?messageId=${msgId}`, {
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
			.then(() => {
				navigate(`/${postId}`);
				window.location.reload();
			})
			.catch((err) => {
				if (err.response.status === 400) {
					if (err.response.data.fieldErrors) {
						alert(err.response.data.fieldErrors[0].reason);
					} else if (
						err.response.data.fieldErrors === null &&
						err.response.data.violationErrors
					) {
						alert(err.response.data.violationErrors[0].reason);
					} else {
						alert(
							"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
						);
					}
				} else if (err.response.status === 0)
					alert(
						"서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
					);
				else {
					if (
						err.response.data.korMessage ===
						"만료된 토큰입니다. 다시 로그인 해주세요."
					) {
						sessionStorage.clear();
						navigate(`/`);
						window.location.reload();
					} else if (err.response.data.korMessage) {
						alert(err.response.data.korMessage);
					} else {
						alert(
							"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
						);
					}
				}
				window.location.reload();
			});
	};

	const readAllMessage = () => {
		axios(
			`${process.env.REACT_APP_URL}/api/messages/read?messageId=${msgIds}`,
			{
				headers: {
					access_hh: sessionStorage.getItem("AccessToken"),
				},
			}
		)
			.then(() => {
				window.location.reload();
			})
			.catch((err) => {
				if (err.response.status === 400) {
					if (err.response.data.fieldErrors) {
						alert(err.response.data.fieldErrors[0].reason);
					} else if (
						err.response.data.fieldErrors === null &&
						err.response.data.violationErrors
					) {
						alert(err.response.data.violationErrors[0].reason);
					} else {
						alert(
							"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
						);
					}
				} else if (err.response.status === 0)
					alert(
						"서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
					);
				else {
					if (
						err.response.data.korMessage ===
						"만료된 토큰입니다. 다시 로그인 해주세요."
					) {
						sessionStorage.clear();
						navigate(`/`);
						window.location.reload();
					} else if (err.response.data.korMessage) {
						alert(err.response.data.korMessage);
					} else {
						alert(
							"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
						);
					}
				}
				window.location.reload();
			});
	};

	const toggleMsg = () => {
		setShowMsg(!showMsg);
	};

	return (
		<HeaderSection>
			{sessionStorage.getItem("isLogin") && (
				<nav>
					<ul className="menuItems">
						<li>
							<p
								onClick={() => {
									sessionStorage.getItem("isLogin")
										? navigate(`/main`)
										: navigate(`/`);
								}}
								data-item="HITCH : HIKER">
								HITCH : HIKER
							</p>
						</li>
						<li>
							<a href="/main" data-item="mainpage">
								mainpage
							</a>
						</li>
						<li>
							<a href="/mypage" data-item="mypage">
								mypage
							</a>
						</li>
						<li>
							<img
								src={sessionStorage.getItem("profileImg")}
								width="40"
								height="40"
								alt=""
							/>
						</li>

						<div className="dropdown">
							<div>{msgIds.length}</div>
							<img
								src={profileImg}
								width="40"
								height="40"
								onClick={toggleMsg}
								alt=""
							/>
							<SubMenu isDropped={showMsg}>
								<ul>
									{showMsg && (
										<li>
											<button
												className="alarm"
												onClick={() => {
													readAllMessage();
												}}>
												전체 읽음
											</button>
										</li>
									)}
									{showMsg &&
										msgs.map((el, idx) => (
											<li
												key={idx}
												onClick={() => {
													msgClickHandler(el.messageId, el.postId);
												}}>
												{el.body}
											</li>
										))}
								</ul>
							</SubMenu>
						</div>
						<li>
							<button className="logoutBtn" onClick={logoutHandler}>
								Logout
							</button>
						</li>
					</ul>
				</nav>
			)}
		</HeaderSection>
	);
};

export default Header;

const SubMenu = styled.div`
	background: gray;
	position: absolute;
	margin-top: 1rem;
	width: 200px;
	left: 50%;
	text-align: center;
	box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.2);
	border-radius: 3px;
	opacity: 0;
	transform: translate(-50%, -10px);
	transition: transform 0.4s ease, visibility 0.4s;
	z-index: 9;

	&:after {
		content: "";
		height: 0;
		width: 0;
		position: absolute;
		top: -3px;
		left: 50%;
		transform: translate(-50%, -50%);
		border: 12px solid transparent;
		border-top-width: 0;
		border-bottom-color: gray;
	}

	${({ isDropped }) =>
		isDropped &&
		css`
			opacity: 1;
			visibility: visible;
			transform: translate(-50%, 0);
		`};
`;

const HeaderSection = styled.div`
	display: grid;
	place-items: center;

	.dropdown {
		position: relative;

		img {
			cursor: pointer;
		}

		.mgs {
			box-sizing: border-box;
			position: absolute;
			margin: 40px;
			padding: 20px;
			border: 1px solid black;
			border-radius: 5px;
			box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.2);
			background: rgba(255, 255, 255, 0.6);
			font-family: Roboto;
			width: fit-content;
			height: fit-content;
		}
	}

	.logoutBtn {
		cursor: pointer;
		margin-left: 1rem;
		padding: 10px;
		border-color: #16213b;
		background: #16213b;
		color: wheat;
		border-radius: 13px;
		&:hover {
			background-color: #304b61;
			border-color: #c2e3de;
		}
	}

	#logout-btn {
		li {
			margin: 50%;
			padding: 1rem;

			a {
				text-decoration: none;
				color: #efd5c8;
				font-size: 3rem;
				font-weight: 400;
				transition: all 0.5s ease-in-out;
				position: relative;
				text-transform: uppercase;

				&::before {
					content: attr(data-item);
					transition: 0.5s;
					color: #efd5c8;
					position: absolute;
					top: 0;
					bottom: 0;
					left: 0;
					right: 0;
					width: 0;
					overflow: hidden;
				}

				&:hover {
					&::before {
						width: 100%;
						transition: all 0.5s ease-in-out;
					}
				}
			}
		}
	}
	body {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		position: relative;
		min-height: 50vh;
		font-family: Hack, monospace;
	}

	button {
		font-size: 1rem;
		background-color: #dabbc9;
		width: fit-content;
		border: 1px solid #dabbc9;
		padding: 0.1rem 1rem;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
		color: #425049;
		&:hover {
			background-color: #efd5c8;
			border-color: #efd5c8;
		}
	}

	div {
		color: #727272;
		text-align: center;
	}

	nav {
		margin-bottom: 1rem;
		box-shadow: 0 1px 4px rgba(1, 1, 0, 0.4);
		background: #d0e8f0;
		opacity: 90%;
		width: 100vw;
		padding: 0.5rem;

		.menuItems {
			list-style: none;
			display: flex;
			align-items: center;
			justify-content: space-between;

			ul {
				text-align: center;
			}
			li {
				margin: 0.5rem;
				margin-right: 3rem;
				display: inline-block;

				p {
					font-size: 24px;
					color: #003046;
					text-transform: uppercase;
					font-weight: 400;
					transition: all 1s ease-in-out;
					position: relative;
					cursor: pointer;

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

					&:hover {
						&::before {
							width: 100%;
						}
					}
				}

				a {
					text-decoration: none;
					color: #8f8f8f;
					font-size: 24px;
					font-weight: 400;
					transition: all 0.5s ease-in-out;
					position: relative;
					text-transform: uppercase;

					&::before {
						content: attr(data-item);
						transition: 0.5s;
						color: #425049;
						position: absolute;
						top: 0;
						bottom: 0;
						left: 0;
						right: 0;
						width: 0;
						overflow: hidden;
					}

					&:hover {
						&::before {
							width: 100%;
							transition: all 0.5s ease-in-out;
						}
					}
				}
			}
		}
	}
`;

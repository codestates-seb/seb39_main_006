import axios from "axios";
import React, { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";

// Toast-UI Viewer 임포트
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import { Editor, Viewer } from "@toast-ui/react-editor";

const PostDetail = () => {
	const navigate = useNavigate();
	const { id } = useParams();

	const [detail, setDetail] = useState([]);
	const [matchList, setMatchList] = useState([]);
	const [matchBody, setMatchBody] = useState("");
	const [isbookmark, setIsBookmark] = useState(false);
	const [mybookmark, setMyBookmark] = useState([]);

	useEffect(() => {
		axios(`${process.env.REACT_APP_URL}/api/posts/${id}`, {
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
			.then((res) => {
				setDetail(res.data);
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
						if (
							err.response.data.korMessage === "존재하지 않는 게시글입니다."
						) {
							alert(err.response.data.korMessage);
							navigate(`/main`);
						}
						alert(err.response.data.korMessage);
					} else {
						alert(
							"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
						);
					}
				}
				window.location.reload();
			});

		axios(`${process.env.REACT_APP_URL}/api/posts/${id}/matching`, {
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
			.then((res) => {
				setMatchList(res.data.data);
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
	}, [id]);

	useEffect(() => {
		axios(`${process.env.REACT_APP_URL}/api/members/my-bookmark`, {
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
			.then((res) => {
				setMyBookmark(res.data.postIds);
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
	}, [isbookmark]);

	useEffect(() => {
		// mybookmark.map((el) => (el === detail.postId ? setIsBookmark(true) : null));
		const bookmarked = mybookmark.includes(detail.postId) ? true : false;
		setIsBookmark(bookmarked);
	}, [mybookmark, detail.postId]);

	const chatHandler = () => {
		axios(
			`${process.env.REACT_APP_URL}/api/chat/rooms/check?checkName=${detail.leaderName}`,
			{
				headers: {
					access_hh: sessionStorage.getItem("AccessToken"),
				},
			}
		)
			.then((res) => {
				if (res.data.roomId) {
					navigate(`/chat/${res.data.roomId}`);
				}
			})
			.catch((err) => {
				if (err.response.status === 500) {
					alert("서버 오류입니다. 다시 시도해주세요.");
					return;
				}
				if (err.response.status !== 0) {
					alert(err.response.data.korMessage);
					return;
				}
				if (err) {
					alert("잘못된 접근 방법입니다. 다시 시도해주세요.");
					return;
				}
			});
	};

	const bookmarkHandler = () => {
		setIsBookmark(!isbookmark);
		axios(
			`${process.env.REACT_APP_URL}/api/members/bookmark?postId=${detail.postId}`,
			{
				headers: {
					access_hh: sessionStorage.getItem("AccessToken"),
				},
			}
		).catch((err) => {
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

	const deleteHandler = () => {
		axios(`${process.env.REACT_APP_URL}/api/posts/${id}`, {
			method: "DELETE",
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
			.then((res) => {
				if (res.headers.access_hh) {
					sessionStorage.setItem("AccessToken", res.headers.access_hh);
				}
				navigate(`/main`);
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
						if (
							err.response.data.korMessage === "존재하지 않는 게시글입니다."
						) {
							alert(err.response.data.korMessage);
							navigate(`/main`);
						}
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

	const matchSubmitHandler = () => {
		axios(`${process.env.REACT_APP_URL}/api/matching/posts/${id}`, {
			method: "POST",
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
			data: { body: matchBody },
		})
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
						if (
							err.response.data.korMessage === "존재하지 않는 게시글입니다."
						) {
							alert(err.response.data.korMessage);
							navigate(`/main`);
						}
						alert(err.response.data.korMessage);
					} else {
						alert(
							"우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
						);
					}
				}
			});
	};

	const kickParticipant = (memberPostId) => {
		axios(`${process.env.REACT_APP_URL}/api/participants/${memberPostId}`, {
			method: "DELETE",
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
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

	const withdrawal = (matchingId) => {
		axios(`${process.env.REACT_APP_URL}/api/matching/${matchingId}`, {
			method: "DELETE",
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
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
						if (
							err.response.data.korMessage === "존재하지 않는 게시글입니다."
						) {
							alert(err.response.data.korMessage);
							navigate(`/main`);
						}
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

	return (
		<PageContainer>
			<ContainerWrap>
				<div className="titleWrapper">
					<span>{detail.title}</span>
					<button
						className="heart"
						onClick={() => {
							bookmarkHandler();
						}}>
						{isbookmark ? "❤️" : "🤍"}
					</button>
					{sessionStorage.getItem("userName") === detail.leaderName ? (
						<div className="postButton">
							<button
								onClick={() => {
									navigate(`/edit/${id}`);
								}}>
								수정
							</button>
							<button
								onClick={() => {
									deleteHandler();
								}}>
								삭제
							</button>
						</div>
					) : null}
				</div>
				<div id="author" className="author">
					{" "}
					{detail.leaderName}
				</div>
				<Container>
					<ContentContainer>
						<FlexContainer>
							<span className="flexbody">
								<span className="span-title">여행일정</span>
								<span className="span-content">
									{detail.startDate} 부터 {detail.endDate} 까지
								</span>
							</span>
							<span className="flexbody">
								<span className="span-title">여행지역</span>
								<span className="span-content">{detail.location}</span>
							</span>
							<span className="flexbody">
								<span className="span-title">매칭기간</span>
								<span className="span-content">{detail.closeDate} 까지</span>
							</span>
							<span className="flexbody">
								<span className="span-title"> 모집 인원</span>
								<span className="span-content">
									{detail.participantsCount} / {detail.totalCount}
								</span>
							</span>
						</FlexContainer>
						<BodyContainer>
							<ReactMarkdown className="viewer">{detail.body}</ReactMarkdown>
						</BodyContainer>
					</ContentContainer>
					<MatchingContainer>
						<div className="participants">
							<p>참여자 명단</p>
							{detail.participants &&
								detail.participants.map((el, idx) => (
									<Match key={idx}>
										<span>
											<div>
												닉네임 : {el.displayName}
												{sessionStorage.getItem("userName") ===
													detail.leaderName &&
												sessionStorage.getItem("userName") !==
													el.displayName ? (
													<button
														onClick={() => {
															kickParticipant(el.memberPostId);
														}}>
														여행 추방
													</button>
												) : null}
												{sessionStorage.getItem("userName") !==
													detail.leaderName &&
												sessionStorage.getItem("userName") ===
													el.displayName ? (
													<button
														onClick={() => {
															kickParticipant(el.memberPostId);
														}}>
														참여 취소
													</button>
												) : null}
											</div>
											<div>자기소개 : {el.content}</div>
										</span>
									</Match>
								))}
						</div>
						<div className="application">
							{sessionStorage.getItem("userName") ===
							detail.leaderName ? null : (
								<Matchtext>
									<p>매칭 신청하기</p>
									<textarea
										placeholder="10글자 이상 작성해주세요."
										onChange={(e) => {
											setMatchBody(e.target.value);
										}}></textarea>
									<button
										onClick={() => {
											matchSubmitHandler();
										}}>
										매칭 신청
									</button>
								</Matchtext>
							)}
						</div>
						<div className="applicants">
							<p>매칭 신청자 명단</p>
							{matchList.map((el, idx) => (
								<Match key={idx}>
									<span>신청자 :</span>
									<span className="hostname"> {el.memberName} </span>
									<span className="isread">
										{sessionStorage.getItem("userName") ===
										detail.leaderName ? (
											<div>
												<button
													className="matching"
													onClick={() => {
														navigate(`/match/${el.matchingId}`, {
															state: el,
														});
													}}>
													매칭관리
												</button>
											</div>
										) : null}
										{sessionStorage.getItem("userName") === el.memberName ? (
											<div>
												<button
													onClick={() => {
														withdrawal(el.matchingId);
													}}>
													신청 취소
												</button>
												<button
													onClick={() => {
														chatHandler();
													}}>
													대화하기
												</button>
											</div>
										) : null}
										{el.matchingStatus === "READ" ? <span>✅</span> : null}
										{el.matchingStatus === "NOT_READ" ? <span>❌</span> : null}
									</span>
								</Match>
							))}
						</div>
					</MatchingContainer>
				</Container>
			</ContainerWrap>
		</PageContainer>
	);
};

export default PostDetail;
const PageContainer = styled.div`
	box-sizing: border-box;
	display: flex;
	flex-direction: column;
	width: 100%;

	/* @media screen and (max-width: fit-content) {
    padding: 30px 25px 30px 25px;
    height: 700px;
  } */
`;

const ContainerWrap = styled.div`
	box-sizing: border-box;
	margin: 3rem auto;
	padding: 1rem;
	width: 87.72% !important;
	max-width: 1080px;
	min-height: 1024px;
	background-color: rgba(255, 255, 255, 0.8);
	box-shadow: 1px 5px 10px rgba(0, 0, 0, 0.1);
	border-radius: 8px;

	.titleWrapper {
		display: flex;
		align-items: center;

		span {
			font-size: 2rem;
			font-weight: 500;
			color: #444;
		}

		.heart {
			border-radius: 50%;
			padding: 5px;
			background-color: white;
			width: 36px;
			height: 36px;
			font-size: 1rem;
			box-sizing: border-box;
		}

		.postButton {
			margin-left: auto;
		}
	}

	#author {
		margin: 0 0.2rem;
		color: darkblue;
		font-weight: 600;
		font-size: 1.3rem;
	}

	button {
		font-size: 1.25rem;
		background-color: #dabbc9;
		width: fit-content;
		border: 1px solid #dabbc9;
		padding: 5px 10px;
		margin: 0.5rem;
		min-width: fit-content;
		box-shadow: 0 2px 2px rgba(0, 0, 0, 0.3);
		color: #425049;
		cursor: pointer;
		&:hover {
			background-color: #efd5c8;
			border-color: #efd5c8;
		}
	}

	.contents {
		padding-left: 1rem;
	}

	@media screen and (max-width: 500px) {
		padding: 30px 25px 30px 25px;
		height: 455px;
	}
`;

const Container = styled.div`
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
	height: 100%;
	grid-gap: 0.5rem;
	gap: 0.5rem;
`;

const ContentContainer = styled.div`
	flex: 2;
	height: 100%;
`;

const FlexContainer = styled.div`
	width: 100%;
	margin: 0.5rem 0 0;
	display: flex;
	justify-content: space-between;

	.span-title {
		font-weight: 600;
		font-size: 1rem;
	}

	.span-content {
		color: darkblue;
		font-weight: 600;
		font-size: 1.125rem;
	}

	.flexbody {
		background: rgba(171, 217, 255, 0.3);
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 25%;
		border-right: 0.025rem solid rgba(0, 0, 0, 0.3);

		&:nth-child(4) {
			border: none;
		}
	}
`;

const BodyContainer = styled.div`
	border-top: 0.1rem solid black;
	width: 100%;

	.viewer {
		padding: 10px;
		font-size: 1.125rem;
		line-height: 150%;
	}
`;

const MatchingContainer = styled.div`
	flex: 1;
	display: flex;
	flex-direction: column;
	padding-left: 0.5rem;
	border-left: 0.05rem solid rgba(0, 0, 0, 0.3);

	p {
		font-size: 1.25rem;
		border-bottom: 0.025rem solid rgba(0, 0, 0, 0.3);
		padding-bottom: 0.25rem;
	}

	button {
		margin-left: auto;
	}

	.participants {
		flex: 1 0 50%;
	}

	.application {
		flex: 2 0 50%;
		margin: 0.5rem 0;
	}

	.applicants {
		flex: 1 0 50%;
	}
`;

const Match = styled.div`
	display: flex;
	background: rgba(171, 217, 255, 0.3);
	margin: 0.5rem auto;

	.hostname {
		font-size: 1.2rem;
	}

	.matching {
		padding: 1rem;
		width: fit-content;
	}

	.isread {
		margin-right: 10px;
	}
	button {
		margin-left: 5px;
		margin-right: 5px;
	}
`;

const Matchtext = styled.div`
	display: flex;
	flex-direction: column;
	textarea {
		flex-grow: 1;
		font-size: 17px;
		height: 200px;
		resize: none;
		border: none;
	}
`;

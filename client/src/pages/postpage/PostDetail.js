import axios from "axios";
import React, { useEffect, useState } from "react";
import { Outlet, useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";

// Toast-UI Viewer 임포트
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import { Viewer } from "@toast-ui/react-editor";

const PostDetail = () => {
	const { id } = useParams();
	const [detail, setDetail] = useState([]);
	const [matchList, setMatchList] = useState([]);
	const [matchBody, setMatchBody] = useState("");
	const [isbookmark, setIsBookmark] = useState(false);
	const [mybookmark, setMyBookmark] = useState([]);
	const navigate = useNavigate();

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
	}, []);

	useEffect(() => {
		mybookmark.map((el) => (el === detail.postId ? setIsBookmark(true) : null));
	}, [mybookmark, detail.postId]);

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

	const goAway = (memberPostId) => {
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

	const goAwayMySelf = (matchingId) => {
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
				<div>
					<div className="titleWrapper">
						<h2>{detail.title}</h2>
						{sessionStorage.getItem("userName") === detail.leaderName ? (
							<>
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
							</>
						) : null}
						<button
							className="heart"
							onClick={() => {
								bookmarkHandler();
							}}>
							{isbookmark ? "❤️" : "🤍"}
						</button>
					</div>
					<p className="author">작성자 :</p>
					<div id="author" className="author">
						{" "}
						{detail.leaderName}
					</div>
					<Container>
						<div>
							<FlexContainer>
								<span className="flexbody">
									<span className="span-title">여행일정</span>
									<span className="span-content">
										{detail.startDate} ~ {detail.endDate}
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
							{detail.body && (
								<div>
									<h2>본문</h2>
									<BodyContainer>
										<Viewer initialValue={detail.body} />
									</BodyContainer>
								</div>
							)}
							{sessionStorage.getItem("userName") ===
							detail.leaderName ? null : (
								<Matchtext>
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

						<div>
							<h2>매칭 신청</h2>
							{matchList.map((el, idx) => (
								<Match key={idx}>
									<span>신청자 :</span>
									<span className="hostname"> {el.memberName} </span>
									<span className="isread">
										{sessionStorage.getItem("userName") ===
										detail.leaderName ? (
											<button
												className="matching"
												onClick={() => {
													navigate(`/match/${el.matchingId}`);
												}}>
												매칭관리
											</button>
										) : null}
										{sessionStorage.getItem("userName") === el.memberName ? (
											<button
												onClick={() => {
													goAwayMySelf(el.matchingId);
												}}>
												신청 취소
											</button>
										) : null}
										{el.matchingStatus === "READ" ? <span>✅</span> : null}
										{el.matchingStatus === "NOT_READ" ? <span>❌</span> : null}
									</span>
								</Match>
							))}
							<h2>참여자 명단</h2>
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
															goAway(el.memberPostId);
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
															goAway(el.memberPostId);
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
					</Container>
				</div>
			</ContainerWrap>
		</PageContainer>
	);
};

export default PostDetail;
const PageContainer = styled.div`
	h2 {
		font-size: 2rem;
		font-weight: 500;
		color: #444;
	}
	box-sizing: border-box;
	position: relative;
	display: flex;
	width: 100%;

	/* @media screen and (max-widt: fit-content) {//여기오타나셨대여 에에에 오타낫대여 오타낫대여 우애애앵!!!!!!!
    padding: 30px 25px 30px 25px;
    height: 700px;
  } */
`;

const ContainerWrap = styled.div`
	/* overflow: scroll; */
	.titleWrapper {
		display: flex;
		align-items: center;
		.heart {
			border-radius: 50%;
			padding: 5px;
			background-color: white;
			width: 36px;
			height: 36px;
			font-size: 1rem;
			box-sizing: border-box;
		}
	}
	#author {
		color: darkblue;
		font-weight: 600;
		font-size: 1.3rem;
	}
	.author {
		font-size: 1.25rem;
	}
	button {
		/* place-items: center; */
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
	box-sizing: border-box;
	position: relative;

	margin: 20px auto 250px;
	//margin: 위 좌우 아래 인가그럼 기억아낮

	padding: 40px 50px 40px 50px;
	/* display: inline-block; */
	/* flex-direction: column; */
	/* min-width: fit-content;
  width: 50%;
  height: fit-content; */
	background-color: rgba(255, 255, 255, 0.8);
	box-shadow: 0px 0px 11px rgba(0, 0, 0, 0.1);
	border-radius: 8px;
	font-family: "Roboto";
	box-sizing: border-box;
	@media screen and (max-width: 500px) {
		padding: 30px 25px 30px 25px;
		height: 455px;
	}
`;
const Match = styled.div`
	.hostname {
		font-size: 1.2rem;
	}
	margin: 1rem;
	.matching {
		padding: 1rem;
		width: fit-content;
	}
	min-width: 23rem;
	display: flex;
	justify-content: space-between;
	border: 1px solid black;
	background-color: #d5eaf1;
	margin-top: 10px;
	margin-bottom: 10px;

	padding: 5px;
	border-radius: 5px;
	.isread {
		margin-right: 10px;
	}
	button {
		margin-left: 5px;
		margin-right: 5px;
	}
`;

const FlexContainer = styled.div`
	.span-title {
		font-weight: 600;
		font-size: 18px;
	}

	.span-content {
		color: darkblue;
		font-weight: 600;
		font-size: 14px;
	}
	width: 100%;
	display: flex;
	justify-content: space-around;

	.flexbody {
		/* border: 1px solid black; */
		border-radius: 15px;
		background-color: #d5eaf1;
		padding: 5px;

		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 25%;
		margin: 0.5rem;
	}
`;

const BodyContainer = styled.div`
	border: 1px solid black;
	background-color: #d5eaf1;
	border-radius: 15px;
	width: 50vw;

	.toastui-editor-contents {
		padding: 10px;
	}
	.toastui-editor-contents p {
		font-size: 17px;
	}
	margin-bottom: 20px;
`;

const Container = styled.div`
	display: flex;
	justify-content: space-around;
	/* background-color: yellow; */
	/* height: 80vh; */
`;

const Matchtext = styled.div`
	display: flex;
	textarea {
		flex-grow: 1;
		font-size: 17px;
		height: 200px;
		margin-right: 20px;
	}
`;

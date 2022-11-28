import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import styled from "styled-components";
const Post = ({ post }) => {
	const navigate = useNavigate();
	const [isbookmark, setIsBookmark] = useState(false);
	const [mybookmark, setMyBookmark] = useState([]);
	const defaultImage = "https://img.seb-006.shop/defaultImage.png";

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
		mybookmark.map((el) => (el === post.postId ? setIsBookmark(true) : null));
	}, [mybookmark, post.postId]);

	const bookmarkHandler = () => {
		setIsBookmark(!isbookmark);
		axios(
			`${process.env.REACT_APP_URL}/api/members/bookmark?postId=${post.postId}`,
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

	return (
		<PostStyle>
			<div className="postBox" onClick={() => navigate(`/${post.postId}`)}>
				{/* 썸네일 영역 */}
				<div className="thumbnail">
					<img
						src={post.thumbnail ? post.thumbnail : defaultImage}
						alt=""></img>
				</div>

				{/* 글 및 내용 요약 영역 */}
				<div className="postContent">
					<p className="postTitle">{post.title}</p>
					<p className="postBody">{post.body}</p>
					<p className="participants">
						모집 인원 {post.participantsCount} / {post.totalCount} |
						<span className="status"> {post.postsStatus} </span>
					</p>
				</div>
			</div>

			{/* 게시글 액션 영역 */}
			<div className="postInfo">
				<span className="location">[{post.location}]</span>
				<span className="leader">{post.leaderName}</span>
				<button
					className="bookmark"
					onClick={() => {
						bookmarkHandler();
					}}>
					{isbookmark ? "❤️" : "🤍"}
				</button>
			</div>
		</PostStyle>
	);
};

export default Post;

const PostStyle = styled.div`
	margin: 0.5rem;
	padding: 0.625rem;
	border-radius: 2px;
	outline: none;

	border-radius: 5px;

	.postBox {
		cursor: pointer;
	}

	.thumbnail {
		cursor: pointer;
		position: relative;
		height: auto;
		width: 100%;
		padding-left: 0;
		padding-bottom: 70%;
		border-radius: 4px;
		border: 1px solid rgba(0, 0, 0, 0.06);
		overflow: hidden;

		img {
			object-fit: cover;
			position: absolute;
			width: 100%;
			height: 100%;
			left: 0;
			top: 0;
			object-position: center;
			z-index: 2;
		}
	}

	.postContent {
		cursor: pointer;
		display: flex;
		flex-direction: column;
		grid-gap: 4px;
		gap: 4px;
		margin-top: 12px;
		padding-right: 4px !important;

		.postTitle {
			font-size: 1.125rem;
			min-height: 2rem;
			overflow: hidden;
			text-overflow: ellipsis;
			font-style: normal;
			font-weight: 600;
			margin: 0;
			word-break: keep-all;
			overflow-wrap: anywhere;
			line-height: 144.4%
			letter-spacing: -.00002em;
		}

		.postBody {
			font-size: 0.9rem;
			min-height: 2rem;
			color: #939393;
			line-height: 138.5%;
			margin: 0;
			font-weight: 400;
			letter-spacing: 0.025em;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
			word-break: keep-all;
			overflow-wrap: anywhere;

			white-space: normal;
			word-wrap: break-word;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
		}


	}

	.participants {
		display: flex;
		align-items: center;
		font-size: 0.9rem;
		font-weight: 600;
		color: #444444;
    }

	.status {
		padding: 0.2rem 0.5rem;
		border: 0.5px solid black;
		border-radius: 8px;
		margin-left: 0.375rem;
	}

	.bookmark {
		margin-left: auto;
	}

	.postInfo {
		display: flex;
		align-items: flex-end;
		grid-gap: 6px;
		gap: 6px;
		padding: 1px 0;

		span {
			color: #888888;
			font-size: 0.9rem;
			line-height: 133.3%;
			font-weight: 600;
			letter-spacing: .032em;

			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;  
		}
	}
`;

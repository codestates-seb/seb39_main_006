import axios from "axios";
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Post from "./Post";
import { useDispatch, useSelector } from "react-redux";
import { filterActions } from "../../store/filter-slice";
import { pageActions } from "../../store/page-slice";
import { useNavigate } from "react-router-dom";

// 페이지네이션
import Pagination from "react-js-pagination";
import "./Paging.css";

const Posts = () => {
	const navigate = useNavigate();
	const dispatch = useDispatch();
	const title = useSelector((state) => state.search.title);
	const body = useSelector((state) => state.search.body);
	const location = useSelector((state) => state.search.location);
	const startDate = useSelector((state) => state.search.startDate);
	const endDate = useSelector((state) => state.search.endDate);
	const sort = useSelector((state) => state.filter.filterValue);
	const page = useSelector((state) => state.page.page);

	const [data, setData] = useState([]);
	const [size, setSize] = useState(12);
	const [totalElements, setTotalElements] = useState(0);

	useEffect(() => {
		axios(
			`${process.env.REACT_APP_URL}/api/posts?page=${page}&size=${size}&title=${title}&body=${body}&location=${location}&startDate=${startDate}&endDate=${endDate}&sort=${sort}`,
			{
				headers: {
					access_hh: sessionStorage.getItem("AccessToken"),
				},
			}
		)
			.then((res) => {
				setData([...res.data.data]);
				setTotalElements(res.data.pageInfo.totalElements);
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
	}, [page, size, title, body, location, startDate, endDate, sort]);

	const handlePageChange = (page) => {
		dispatch(pageActions.setPage(page));
	};

	return (
		<StyledPost>
			<div className="wrapper">
				<div className="btnflex">
					<button
						className={sort === "newest" ? "filterbtn focusbtn" : "filterbtn"}
						value="newest"
						onClick={(e) => {
							dispatch(filterActions.setFilter(e.target.value));
						}}>
						최신순(기본값)
					</button>
					<button
						className={
							sort === "startDate" ? "filterbtn focusbtn" : "filterbtn"
						}
						value="startDate"
						onClick={(e) => {
							dispatch(filterActions.setFilter(e.target.value));
						}}>
						여행 시작 날짜 순
					</button>
					<button
						className={sort === "endDate" ? "filterbtn focusbtn" : "filterbtn"}
						value="endDate"
						onClick={(e) => {
							dispatch(filterActions.setFilter(e.target.value));
						}}>
						여행 종료 날짜 순
					</button>
					<button
						className={
							sort === "closeDate" ? "filterbtn focusbtn" : "filterbtn"
						}
						value="closeDate"
						onClick={(e) => {
							dispatch(filterActions.setFilter(e.target.value));
						}}>
						모집 종료 날짜 순
					</button>
					<button
						className={
							sort === "totalCount" ? "filterbtn focusbtn" : "filterbtn"
						}
						value="totalCount"
						onClick={(e) => {
							dispatch(filterActions.setFilter(e.target.value));
						}}>
						모집 인원 순
					</button>
					<button
						className={sort === "limited" ? "filterbtn focusbtn" : "filterbtn"}
						value="limited"
						onClick={(e) => {
							dispatch(filterActions.setFilter(e.target.value));
						}}>
						남은 인원 순
					</button>
				</div>
				<ul className="contents">
					{data.map((post) => (
						<li key={post.postId}>
							<Post post={post} />
						</li>
					))}
				</ul>
				<Pagination
					activePage={page}
					itemsCountPerPage={size}
					totalItemsCount={totalElements}
					pageRangeDisplayed={5}
					prevPageText={"‹"}
					nextPageText={"›"}
					onChange={handlePageChange}
				/>
			</div>
		</StyledPost>
	);
};

export default Posts;
const StyledPost = styled.div`
	margin: auto;
	.wrapper {
		font-family: "Segoe UI", Roboto;
		background-color: #d5eaf1;
		border-radius: 10px;
		box-shadow: 0px 3px 10px 1px rgba(0, 0, 0, 0.3);
		z-index: -1;
	}
	ul {
		list-style: none;
		padding: 0;
		margin: 0;
		border: 0;
	}
	li {
		display: list-item;
		text-align: -webkit-match-parent;
		position: relative;
		width: calc((100% / 3));
		padding: 0;
		margin: 0;
		border: 0;
	}
	.contents {
		display: flex;
		flex-wrap: wrap;
		width: 100% !important;
		max-width: 1080px;
		margin: 0 auto;
		margin-bottom: 50px;
		list-style-type: none;

		@media (min-width: 1200px) {
			max-width: 1080px;
			width: 87.72%;
		}
	}
	button {
		cursor: pointer;
		font-size: 1rem;
		background-color: #dabbc8;
		width: fit-content;
		border: 1px solid #dabbc8;
		padding: 0.5rem 1rem;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
		color: #425049;
		&:hover {
			background-color: #efd5c8;
			border-color: #efd5c8;
		}
	}
	.btnflex {
		display: flex;
		justify-content: space-between;
	}
	.filterbtn {
		flex-grow: 1/0;
		border-radius: 8px;
		margin: 5px;
	}
	.focusbtn {
		background-color: white;
	}
`;

import axios from "axios";
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Post from "./Post";
import { useDispatch, useSelector } from "react-redux";
import { filterActions } from "../../store/filter-slice";
import { pageActions } from "../../store/page-slice";

// 페이지네이션
import Pagination from "react-js-pagination";
import "./Paging.css";

const Posts = () => {
  const dispatch = useDispatch();
  const title = useSelector((state) => state.search.title);
  const body = useSelector((state) => state.search.body);
  const location = useSelector((state) => state.search.location);
  const startDate = useSelector((state) => state.search.startDate);
  const endDate = useSelector((state) => state.search.endDate);
  const sort = useSelector((state) => state.filter.filterValue);
  const page = useSelector((state) => state.page.page);

  const [data, setData] = useState([]);
  const [size, setSize] = useState(2);
  const [totalElements, setTotalElements] = useState(0);

  useEffect(() => {
    axios(
      `${process.env.REACT_APP_URL}/api/posts?page=${page}&size=${size}&title=${title}&body=${body}&location=${location}&startDate=${startDate}&endDate=${endDate}&sort=${sort}`
    ).then((res) => {
      setData([...res.data.data]);
      setTotalElements(res.data.pageInfo.totalElements);
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
            }}
          >
            최신순(기본값)
          </button>
          <button
            className={
              sort === "startDate" ? "filterbtn focusbtn" : "filterbtn"
            }
            value="startDate"
            onClick={(e) => {
              dispatch(filterActions.setFilter(e.target.value));
            }}
          >
            여행 시작 날짜 순
          </button>
          <button
            className={sort === "endDate" ? "filterbtn focusbtn" : "filterbtn"}
            value="endDate"
            onClick={(e) => {
              dispatch(filterActions.setFilter(e.target.value));
            }}
          >
            여행 종료 날짜 순
          </button>
          <button
            className={
              sort === "closeDate" ? "filterbtn focusbtn" : "filterbtn"
            }
            value="closeDate"
            onClick={(e) => {
              dispatch(filterActions.setFilter(e.target.value));
            }}
          >
            모집 종료 날짜 순
          </button>
          <button
            className={
              sort === "totalCount" ? "filterbtn focusbtn" : "filterbtn"
            }
            value="totalCount"
            onClick={(e) => {
              dispatch(filterActions.setFilter(e.target.value));
            }}
          >
            모집 인원 순
          </button>
          <button
            className={sort === "limited" ? "filterbtn focusbtn" : "filterbtn"}
            value="limited"
            onClick={(e) => {
              dispatch(filterActions.setFilter(e.target.value));
            }}
          >
            남은 인원 순
          </button>
        </div>
        <div className="contents">
          {data.map((post) => (
            <div key={post.postId} className="post">
              <Post post={post} />
            </div>
          ))}
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
      </div>
    </StyledPost>
  );
};

export default Posts;
const StyledPost = styled.div`
  display: inline-block;
  .wrapper {
    flex-grow: 1;
    /* width: 750px; */
    width: 78%;
    height: 90vh;
    margin-right: 20px;
    background-color: #d5eaf1;
    border-radius: 10px;
    position: absolute;
    box-shadow: 0px 3px 10px 1px rgba(0, 0, 0, 0.3);
  }
  .contents {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .post {
    display: flex, inline-flex;
    justify-content: center;
    margin: 1rem;
    padding: 10px;
    border-radius: 2px;
    outline: none;
    width: 75vw;
    border-radius: 5px;
    border: 1.5px solid #a19f9f;
    background-color: white;
    font-size: large;
  }
  button {
    font-size: 1rem;
    background-color: #dabbc9;
    width: fit-content;
    border: 1px solid #dabbc9;
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
    flex-grow: 1;
    border-radius: 15px;
    margin: 5px;
  }
  .focusbtn {
    background-color: white;
  }
`;

import axios from "axios";
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Post from "./Post";

// 페이지네이션
import Pagination from "react-js-pagination";
import "./Paging.css";
import { useDispatch, useSelector } from "react-redux";
import { filterActions } from "../../store/filter-slice";

const Posts = () => {
  const dispatch = useDispatch();
  const title = useSelector((state) => state.search.title);
  const body = useSelector((state) => state.search.body);
  const location = useSelector((state) => state.search.location);
  const startDate = useSelector((state) => state.search.startDate);
  const endDate = useSelector((state) => state.search.endDate);
  const sort = useSelector((state) => state.filter.filterValue);

  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  // 테스트 푸시
  // 테스트 푸시22
  useEffect(() => {
    axios(
      `https://seb-006.shop/api/posts?page=${page}&size=${size}&title=${title}&body=${body}&location=${location}&startDate=${startDate}&endDate=${endDate}&sort=${sort}`
    ).then((res) => {
      setData([...res.data.data]);
      setTotalElements(res.data.pageInfo.totalElements);
    });
  }, [page, size, title, body, location, startDate, endDate, sort]);

  const handlePageChange = (page) => {
    setPage(page);
  };

  return (
    <StyledPost>
      <button
        value="newest"
        onClick={(e) => dispatch(filterActions.setFilter(e.target.value))}
      >
        최신순(기본값)
      </button>
      <button
        value="startDate"
        onClick={(e) => dispatch(filterActions.setFilter(e.target.value))}
      >
        여행 시작 날짜 순
      </button>
      <button
        value="endDate"
        onClick={(e) => dispatch(filterActions.setFilter(e.target.value))}
      >
        여행 종료 날짜 순
      </button>
      <button
        value="closeDate"
        onClick={(e) => dispatch(filterActions.setFilter(e.target.value))}
      >
        모집 종료 날짜 순
      </button>
      <button
        value="totalCount"
        onClick={(e) => dispatch(filterActions.setFilter(e.target.value))}
      >
        모집 인원 순
      </button>
      <button
        value="limited"
        onClick={(e) => dispatch(filterActions.setFilter(e.target.value))}
      >
        남은 인원 순
      </button>
      {data.map((post) => (
        <div key={post.postId}>
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
    </StyledPost>
  );
};

export default Posts;
const StyledPost = styled.div`
  display: inline-block;
  .wrapper {
    width: 750px;
    height: 700px;
    background-color: #d5eaf1;
    border-radius: 10px;
    position: absolute;
    box-shadow: 0px 3px 10px 1px rgba(0, 0, 0, 0.3);
  }
  .contents {
    padding: 3.5em;
    position: relative;
    height: 65%;
  }
  .post {
    margin: 1rem;
    padding: 10px;
    border-radius: 2px;
    outline: none;
    width: 38rem;
    display: inline-block;
    border: none;
    border-radius: 5px;
    border: 1.5px solid #a19f9f;
    background-color: white;
    font-size: large;
  }
`;

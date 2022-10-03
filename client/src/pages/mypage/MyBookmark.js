import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SideBar from "../../components/SideBar";

const MyBookmark = () => {
  const [bookmarkData, setBookmarkData] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`https://seb-006.shop/api/members/bookmarked`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then((res) => {
      setBookmarkData([...res.data.data]);
    });
  }, []);

  return (
    <div>
      <SideBar />
      <h1>북마크</h1>
      {bookmarkData.map((el, idx) => (
        <div key={idx}>
          <h2
            onClick={() => {
              navigate(`/${el.postId}`);
            }}
          >
            {el.title} 모집인원 {el.participantsCount} / {el.totalCount}
          </h2>
        </div>
      ))}
    </div>
  );
};

export default MyBookmark;

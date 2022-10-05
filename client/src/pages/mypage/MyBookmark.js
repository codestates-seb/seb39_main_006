import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SideBar from "../../components/SideBar";
import H1 from "../../components/ui/H1";
const MyBookmark = () => {
  const [bookmarkData, setBookmarkData] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/members/bookmarked`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then((res) => {
      setBookmarkData([...res.data.data]);
    });
  }, []);

  return (
    <div>
      <SideBar />
      <H1>북마크</H1>
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

import axios from "axios";
import React, { useEffect, useState } from "react";
import H1 from "../../components/ui/H1";
import Button from "../../components/ui/Button";
import { useNavigate } from "react-router-dom";
import Feed from "./Feed";

const Feeds = () => {
  const navigate = useNavigate();
  const [feeds, setFeeds] = useState([]);

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/feeds?page=1&size=10`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then((res) => {
      console.log(res.data.data);
      setFeeds(res.data.data);
    });
  }, []);
  return (
    <div>
      <H1>기능구현중입니다~</H1>
      <Button
        onClick={() => {
          navigate(`/newfeed`);
        }}
      >
        피드 작성하기
      </Button>
      {feeds.map((feed) => (
        <div key={feed.feedId}>
          <Feed feed={feed} />
        </div>
      ))}
    </div>
  );
};

export default Feeds;

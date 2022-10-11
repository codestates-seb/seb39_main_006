import React from "react";

const Feed = ({ feed }) => {
  return (
    <div>
      <div>
        {feed.body}{" "}
        <img
          src={feed.profileImage ? feed.profileImage : ""}
          width="30"
          height="30"
        />
        작성자 : {feed.memberName}
      </div>
    </div>
  );
};

export default Feed;

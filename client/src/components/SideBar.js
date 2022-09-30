import React from "react";
import styled from "styled-components";

const SideBar = () => {
  return (
    <SideBarBorder>
      <div>사이드바</div>
      <div>사이드바</div>
      <div>사이드바</div>
      <div>사이드바</div>
      <div>사이드바</div>
      <div>사이드바</div>
    </SideBarBorder>
  );
};

export default SideBar;
const SideBarBorder = styled.div`
  border: 1px solid black;
  width: 200px;
`;

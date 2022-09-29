import React from "react";
import styled from "styled-components";

const SideBar = () => {
  return <SideBarBorder>사이드바 아마 지역이 들어가겠지...?</SideBarBorder>;
};

export default SideBar;
const SideBarBorder = styled.div`
  border: 1px solid black;
  width: 200px;
`;

import React from "react";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";

const MainSubHeader = () => {
  const navigate = useNavigate();
  return (
    <div>
      <Input></Input>
      <Button>검색</Button>
      <Button
        onClick={() => {
          navigate(`/new`);
        }}
      >
        게시글 작성
      </Button>
    </div>
  );
};

export default MainSubHeader;

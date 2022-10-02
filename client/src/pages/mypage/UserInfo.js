import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
const Userinfo = () => {
  const navigate = useNavigate();
  const userinfoHandler = (memberId) => {
    navigate(`/${memberId}`);
  };
  return (
    <>
      <h1>userinfo</h1>
      <Button onClick={userinfoHandler}></Button>
    </>
  );
};

export default Userinfo;

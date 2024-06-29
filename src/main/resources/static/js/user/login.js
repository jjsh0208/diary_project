const hi = () =>{
    let userId = document.querySelector('[name="id"]').value;
    let userPw = document.querySelector('[name="pw"]').value;
    if(userId === "" || userPw === ""){
        alert("빈칸이 있습니다.");
        return;
    }
}
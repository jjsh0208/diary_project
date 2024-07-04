// 모달 요소를 가져옵니다
var videoModal = document.getElementById("videoModal");
var resultsModal = document.getElementById("resultsModal");

// 동영상을 여는 함수입니다
function openVideo(element) {
    videoModal.style.display = "block";
    var url = element.getAttribute('data-url');
    document.getElementById("videoFrame").src = url;
}

// <span> (x)를 클릭하면 모달을 닫습니다
function closeModal(){
    videoModal.style.display = "none";
    // 모달이 닫힐 때 iframe의 src를 비워서 동영상 재생을 중지합니다
    document.getElementById("videoFrame").src = "";
}

// 사용자가 모달 영역 바깥을 클릭하면 모달을 닫습니다
window.onclick = function(event) {
    if (event.target == videoModal) {
        closeModal();
    }
}

// 사용자에게 음악 선택 확인하는 confirm 커스텀
function customConfirm(element, callback) {
    // 대화 상자를 HTML 요소로 생성(동적)
    var confirmBox = document.createElement('div');
    confirmBox.setAttribute('class', 'confirm-box');
    confirmBox.innerHTML = '<p>' + element.getAttribute('data-key') + '를 선택하시겠습니까?</p><button class="confirm">네</button><button class="cancel">아니요</button>';

    // HTML 요소를 body 요소의 하위 요소로 추가합니다.
    document.body.appendChild(confirmBox);

    // 배경을 회색으로 덮어서 모달 창을 띄웠을 때 다른 요소들을 클릭할 수 없도록 제어
    var overlay = document.createElement('div');
    overlay.setAttribute('class', 'overlay');
    document.body.appendChild(overlay);

    var removeAlert = function() {
        document.body.removeChild(confirmBox);
        document.body.removeChild(overlay);
        window.removeEventListener('keydown', handleKeyDown);
    };

    var handleKeyDown = function(event) {
        event.preventDefault();
    };

    // 확인 버튼을 클릭했을 때 이벤트
    return new Promise(function(resolve) {
        var confirmButton = document.querySelector('.confirm');
        confirmButton.addEventListener('click', function() {
            // 확인 버튼을 눌렀을 때 resolve 메서드를 호출
            removeAlert();
            setTimeout(function() {
                resolve(true);
            }, 100);
        });

        // 취소 버튼을 클릭했을 때 이벤트
        var cancelButton = document.querySelector('.cancel');
        cancelButton.addEventListener('click', function() {
            // 취소 버튼을 눌렀을 때 resolve 메서드를 호출
            removeAlert();
            setTimeout(function() {
                resolve(false);
            }, 100);
        });

        // 회색 영역 클릭할 때 창을 닫도록 이벤트 추가
        overlay.addEventListener('click', function() {
            removeAlert();
        });

        // 키를 눌렀을 때 기본 동작을 막습니다.
        window.addEventListener('keydown', handleKeyDown);
    });

}

// 함수 호출
function Click_bth(message, element){
    var videoUrl = element.getAttribute('data-url');
    var videoTitle = element.getAttribute('data-key')

    customConfirm(message)
        .then(function(result){
            if (result) {
                // localstorage에 선택된 비디오 링크 저장
                localStorage.setItem('selectedVideoLink', videoUrl);
                // // 확인 버튼을 눌렀을 때 동작
                window.location.href = '/diary/write';
            } else {
                // 취소 버튼을 눌렀을 때 동작
                console.log('취소 클릭');
            }
        });
}

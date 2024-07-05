var musicPlayerModal = document.getElementById("musicPlayerModal");

// youtube iframe player api를 비동기로 로드
var tag = document.createElement("script");
tag.src =  "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

let player;

// 플레이어가 준비된 후 호출되는 콜백 함수
function onPlayerReady(event) {
    console.log("Player ready");
    event.target.playVideo();
}

// 모달 닫기 버튼 처리
function closeMusicPlayerModal(){
    musicPlayerModal.style.display = "none";
    // 비디오 정지
    if(player)
        player.stopVideo();
}

// 사용자가 모달 영역 바깥을 클릭하면 모달을 닫습니다
window.onclick = function(event) {
    if (event.target == musicPlayerModal) {
        closeMusicPlayerModal();
    }
}

const musicWrap = document.querySelector(".wrap_music");
const musicPlay = musicWrap.querySelector("#control-play");
const musicProgress = musicWrap.querySelector(".music_progress");
const musicProgressBar = musicProgress.querySelector(".bar");
const musicProgressCurrent = musicProgress.querySelector(".current");
const musicProgressDuration = musicProgress.querySelector(".duration");

// musicUrl hidden input 요소에서 값을 가져옴
const musicUrlInput = document.getElementById("musicUrl");
const musicUrl = musicUrlInput.value;

function playMusic(){
    var str = musicUrl.split('=');
    var videoId = str[1];

    musicWrap.classList.add("paused");
    musicPlay.innerHTML = "<i class='material-icons'>pause</i>";
    musicPlay.setAttribute("title", "일시정지")

    if(!player && videoId){
        // 모달 띄우기
        //musicPlayerModal.style.display = "block";
        player = new YT.Player('musicPlayer', {
            height: '315',
            width: '560',
            videoId: videoId,
            playerVars: {
                'autoplay': 1,
                'controls': 1,
                'autohide': 1,
                'showinfo': 0,
                'modestbranding': 1,
                'rel': 0,
                'loop': 1
            },
            events: {
                onReady: onPlayerReady,
                onStateChange: onPlayerStateChange
            }
        });
    }else if(player){
        player.playVideo();
    }
}

// 일시정지 버튼
function pauseMusic(){
    musicWrap.classList.remove("paused");
    musicPlay.innerHTML = "<i class='material-icons'>play_arrow</i>";
    musicPlay.setAttribute("title", "재생")
    if(player)
        player.pauseVideo();
}

// 재생/일시정지
musicPlay.addEventListener("click", ()=>{
    const isMusicPaused = musicWrap.classList.contains("paused");
    isMusicPaused ? pauseMusic() : playMusic();
});

// 플레이어 상태 변화 이벤트 처리
function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING) {
        updateProgressBar();
    }
}

// 진행 바 클릭 시 재생 위치 이동
musicProgress.addEventListener("click", (event) => {
    const progressBarWidth = musicProgress.clientWidth;
    const clickPositionX = event.offsetX;
    const songDuration = player.getDuration();

    const seekTo = (clickPositionX / progressBarWidth) * songDuration;
    player.seekTo(seekTo);
});

// 진행 바 업데이트
function updateProgressBar() {
    const currentTime = player.getCurrentTime();
    const duration = player.getDuration();
    const progress = (currentTime / duration) * 100;

    musicProgressBar.style.width = progress + "%";

    // 현재 재생 시간 포맷팅
    const currentMinutes = Math.floor(currentTime / 60);
    const currentSeconds = Math.floor(currentTime % 60);
    const durationMinutes = Math.floor(duration / 60);
    const durationSeconds = Math.floor(duration % 60);

    musicProgressCurrent.textContent = `${currentMinutes}:${currentSeconds < 10 ? '0' + currentSeconds : currentSeconds}`;
    musicProgressDuration.textContent = `${durationMinutes}:${durationSeconds < 10 ? '0' + durationSeconds : durationSeconds}`;

    // 재생이 끝나면 진행 바 초기화
    if (currentTime >= duration) {
        musicProgressBar.style.width = "0%";
    } else {
        requestAnimationFrame(updateProgressBar);
    }
}

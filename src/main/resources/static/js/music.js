var musicPlayerModals = {};
var players = {};
var currentPlayingEntryId = null;

// YouTube iframe player API를 비동기로 로드
var tag = document.createElement("script");
tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// 플레이어가 준비된 후 호출되는 콜백 함수
function onPlayerReady(event) {
    console.log("Player ready");
    event.target.playVideo();
}

// 모달 닫기 버튼 처리
function closeMusicPlayerModal(entryId) {
    var musicPlayerModal = musicPlayerModals[entryId];
    musicPlayerModal.style.display = "none";
    // 비디오 정지
    if (players[entryId]) {
        players[entryId].stopVideo();
    }
}

// 사용자가 모달 영역 바깥을 클릭하면 모달을 닫습니다
window.onclick = function(event) {
    Object.values(musicPlayerModals).forEach(modal => {
        if (event.target == modal) {
            closeMusicPlayerModal(modal.getAttribute("data-entry"));
        }
    });
}

document.querySelectorAll(".wrap_music").forEach(musicWrap => {
    const entryId = musicWrap.getAttribute("data-entry");
    const musicPlay = musicWrap.querySelector("#control-play" + entryId);
    const musicProgress = musicWrap.querySelector(".music_progress");
    const musicProgressBar = musicWrap.querySelector("#progress-bar" + entryId);
    const musicProgressCurrent = musicWrap.querySelector("#current-time" + entryId);
    const musicProgressDuration = musicWrap.querySelector("#duration-time" + entryId);
    const musicUrlInput = document.getElementById("musicUrl" + entryId);
    const musicUrl = musicUrlInput.value;

    musicPlayerModals[entryId] = document.getElementById("musicPlayerModal" + entryId);

    function playMusic() {
        var str = musicUrl.split('=');
        var videoId = str[1];

        // 현재 재생 중인 플레이어를 정지
        if (currentPlayingEntryId && currentPlayingEntryId !== entryId) {
            pauseMusic(currentPlayingEntryId);
        }

        musicWrap.classList.add("paused");
        musicPlay.innerHTML = "<i class='material-icons'>pause</i>";
        musicPlay.setAttribute("title", "일시정지");

        if (!players[entryId] && videoId) {
            players[entryId] = new YT.Player('musicPlayer' + entryId, {
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
        } else if (players[entryId]) {
            players[entryId].playVideo();
        }

        currentPlayingEntryId = entryId;
    }

    function pauseMusic(id) {
        const wrap = document.querySelector(`[data-entry="${id}"]`);
        const playButton = wrap.querySelector(`#control-play${id}`);

        wrap.classList.remove("paused");
        playButton.innerHTML = "<i class='material-icons'>play_arrow</i>";
        playButton.setAttribute("title", "재생");

        if (players[id]) {
            players[id].pauseVideo();
        }
    }

    musicPlay.addEventListener("click", () => {
        const isMusicPaused = musicWrap.classList.contains("paused");
        isMusicPaused ? pauseMusic(entryId) : playMusic();
    });

    function onPlayerStateChange(event) {
        if (event.data == YT.PlayerState.PLAYING) {
            updateProgressBar(entryId);
        }
    }

    musicProgress.addEventListener("click", (event) => {
        const progressBarWidth = musicProgress.clientWidth;
        const clickPositionX = event.offsetX;
        const songDuration = players[entryId].getDuration();
        const seekTo = (clickPositionX / progressBarWidth) * songDuration;
        players[entryId].seekTo(seekTo);
    });

    function updateProgressBar(id) {
        const currentTime = players[id].getCurrentTime();
        const duration = players[id].getDuration();
        const progress = (currentTime / duration) * 100;
        const bar = document.querySelector(`#progress-bar${id}`);
        const current = document.querySelector(`#current-time${id}`);
        const durationTime = document.querySelector(`#duration-time${id}`);

        bar.style.width = progress + "%";

        const currentMinutes = Math.floor(currentTime / 60);
        const currentSeconds = Math.floor(currentTime % 60);
        const durationMinutes = Math.floor(duration / 60);
        const durationSeconds = Math.floor(duration % 60);

        current.textContent = `${currentMinutes}:${currentSeconds < 10 ? '0' + currentSeconds : currentSeconds}`;
        durationTime.textContent = `${durationMinutes}:${durationSeconds < 10 ? '0' + durationSeconds : durationSeconds}`;

        if (currentTime >= duration) {
            bar.style.width = "0%";
        } else {
            requestAnimationFrame(() => updateProgressBar(id));
        }
    }
});

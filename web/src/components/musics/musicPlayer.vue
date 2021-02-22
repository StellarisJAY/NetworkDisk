<template>
    <el-row>
        <el-col :span="7">
            <el-popover placement="bottom">
                <el-table :data="playList" height="200px">
                    <el-table-column label="名称" prop="filename"></el-table-column>
                    <el-table-column label="操作" prop="filename">
                        <template slot-scope="scope">
                            <a @click="removeFromPlayList(scope.row)">x</a>
                        </template>
                    </el-table-column>
                </el-table>
                <el-button type="primary" circle size="mini" slot="reference">
                    <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-list-ul" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd" d="M5 11.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zm-3 1a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm0 4a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm0 4a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"/>
                    </svg>
                </el-button>
            </el-popover>
            <!--暂停/继续按钮-->
            <el-button type="primary" circle size="small" v-if="music.isPlay==false" @click="continueMusic">
                <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-play-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path d="M11.596 8.697l-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393z"/>
                </svg>
            </el-button>
            <el-button type="primary" circle size="small" v-else @click="pauseMusic">
                <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-pause-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path d="M5.5 3.5A1.5 1.5 0 0 1 7 5v6a1.5 1.5 0 0 1-3 0V5a1.5 1.5 0 0 1 1.5-1.5zm5 0A1.5 1.5 0 0 1 12 5v6a1.5 1.5 0 0 1-3 0V5a1.5 1.5 0 0 1 1.5-1.5z"/>
                </svg>
            </el-button>
            <!--音量调节按钮-->
            <el-popover placement="right">
                <el-slider vertical height="100px" max="100"></el-slider>
                <el-button type="primary" circle size="mini" slot="reference">
                    <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-volume-up-fill" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                        <path d="M11.536 14.01A8.473 8.473 0 0 0 14.026 8a8.473 8.473 0 0 0-2.49-6.01l-.708.707A7.476 7.476 0 0 1 13.025 8c0 2.071-.84 3.946-2.197 5.303l.708.707z"/>
                        <path d="M10.121 12.596A6.48 6.48 0 0 0 12.025 8a6.48 6.48 0 0 0-1.904-4.596l-.707.707A5.483 5.483 0 0 1 11.025 8a5.483 5.483 0 0 1-1.61 3.89l.706.706z"/>
                        <path d="M8.707 11.182A4.486 4.486 0 0 0 10.025 8a4.486 4.486 0 0 0-1.318-3.182L8 5.525A3.489 3.489 0 0 1 9.025 8 3.49 3.49 0 0 1 8 10.475l.707.707z"/>
                        <path fill-rule="evenodd" d="M6.717 3.55A.5.5 0 0 1 7 4v8a.5.5 0 0 1-.812.39L3.825 10.5H1.5A.5.5 0 0 1 1 10V6a.5.5 0 0 1 .5-.5h2.325l2.363-1.89a.5.5 0 0 1 .529-.06z"/>
                    </svg>
                </el-button>
            </el-popover>
        </el-col>
        <!-- 播放器进度条-->
        <el-col :span="10">
            <el-slider v-model="music.currentTime" :max="music.maxTime" :format-tooltip="formatTime"  @change="onMusicSliderChange"></el-slider>
        </el-col>
        <!-- 播放器时间显示-->
        <el-col :span="7" style="padding: 9px 0px 0px 10px;color:#909399;font-size: 13px">
            {{formatTime(music.currentTime)}}/{{formatTime(music.maxTime)}}
        </el-col>
        <!-- 音频对象-->
        <audio id="musicPlayer" ref="music" autoplay :src="music.url">
        </audio>
    </el-row>
</template>

<script>
import request from '../../utils/request'
import eventBus from '../../utils/eventBus'
export default {
    name: "MusicPlayer",
    data(){
        return {
            music: {
                isPlay: false,
                currentTime: 0,
                maxTime: 0,
                volume: 0.5,
                url: ""
            },
            playList:[{filename: "test.mp3"},
            {filename: "test.mp3"},
            {filename: "test.mp3"},
            {filename: "test.mp3"},
            {filename: "test.mp3"},
            {filename: "test.mp3"} ]
        }
    },
    created(){
        console.log("created")
    },
    mounted(){
        // 设置每秒监听一次播放情况
        this.$nextTick(()=>{
            setInterval(this.listenMusic, 1000)
        })
        let that = this
        // 监听“playMusic”事件，父组件用该事件唤醒该组件
        eventBus.$on("playMusic", (file)=>{
            console.log(file)
            that.playMusic(file)
        })
    },
    methods: {
        onMusicSliderChange(val){
            this.$refs.music.currentTime = val
        },
        /**
         * 暂停播放
         */
        pauseMusic(){
            if(this.$refs.music != undefined && this.$refs.music.paused == false){
                this.music.isPlay = false
                this.$refs.music.pause()
            }
        },
        /**
         * 继续播放
         */
        continueMusic(){
            if(this.$refs.music != undefined && this.$refs.music.paused == true){
                this.music.isPlay = true
                this.$refs.music.play()
            }
        },
        /**
         * 监听播放过程，改变页面内容
         */
        listenMusic(){
            if(this.$refs.music != undefined){
                this.music.isPlay = !this.$refs.music.paused
                // 刷新音量
                this.$refs.music.volume = this.music.volume
                // 刷新时间显示
                if(!isNaN(this.$refs.music.duration)){
                    this.music.currentTime = parseInt(this.$refs.music.currentTime)
                    this.music.maxTime = parseInt(this.$refs.music.duration)
                }
                else{
                    this.music.currentTime = 0
                    this.music.maxTime = 0
                }
            }
        },
        /**
         * 加载音乐，开始播放
         */
        playMusic(file){
            let that = this
            // 从后端加载音乐blob
            request.get("/file/download/"+file.fileId, {responseType: "blob"})
            .then((data)=>{
                // 生成url
                let url = window.URL.createObjectURL(new Blob([data]))
                that.music.url = url
                that.music.isPlay = true
                // 加载音频对象
                that.$refs.music.load()
            })
        },
        /**
         * 转化时间显示格式
         */
        formatTime(value){
            let hours = Math.floor(value >= 3600 ? value / 3600 : 0)

            let minutes = Math.floor((value % 3600)/60)
            
            let seconds = Math.round((value % 3600) % 60)
            
            if(hours != 0){
                return (hours >= 10 ? hours : '0'+hours) + ':' + (minutes >= 10 ? minutes : '0'+minutes) + ':' + (seconds >= 10 ? seconds : '0'+seconds)
            }
            else if(minutes != 0){
                return (minutes >= 10 ? minutes : '0'+minutes) + ':' + (seconds >= 10 ? seconds : '0'+seconds)
            }
            else{
                return '00:' + (seconds >= 10 ? seconds : '0'+seconds)
            }
        }
    }
}
</script>

<style>

</style>
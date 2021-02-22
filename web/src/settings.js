export default {
    tokenKey: 'NDSKTOKEN',
    timeout: 30000,
    baseUrl: 'http://localhost:8001',
    playList: [],
    iconUrls:{
        directory: '../../static/image/folder.png',
        misc: '../../static/image/misc.png',
        picture: '../../static/image/picture.png',
        zip: '../../static/image/zip.png',
        pdf: '../../static/image/pdf.png',
        docx: '../../static/image/word.png',
        txt: '../../static/image/txt.png',
        ppt: '../../static/image/ppt.png',
        excel: '../../static/image/excel.png',
        music: '../../static/image/music.png'
    },
    getIconUrl(type){
        if(type == 'directory' || type=='pdf'){
            return this.iconUrls[type]
        }
        else if(type=='png' || type=='jpg' || type=='gif'){
            return this.iconUrls['picture']
        }
        else if(type=='zip' || type=='rar' || type=='gzip' || type=='7z'){
            return this.iconUrls['zip']
        }
        else if(type=='docx' || type=='doc'){
            return this.iconUrls['docx']
        }
        else if(type=='xlsx' || type=='xls'){
            return this.iconUrls['excel']
        }
        else if(type=='pptx' || type=='ppt'){
            return this.iconUrls['ppt']
        }
        else if(type=='txt'){
            return this.iconUrls['txt']
        }
        else if(type == 'mp3' || type=='wav' || type=='flac'){
            return this.iconUrls['music']
        }
        else{
            return this.iconUrls['misc']
        }
    }
}
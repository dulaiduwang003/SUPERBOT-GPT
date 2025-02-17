Component({
    properties: {
        url: {
            type: String,
            value: ''
        },
        a: Number,
    },
    data: {
        loaded: false
    },
    lifetimes: {
        attached() {
            console.log('data.a', this.data.a)
        }
    },
    methods: {
        handleReady: function({detail}) {
            this.scene = detail.value;
            console.log('scene', detail.value);
        },
        handleAssetsProgress: function({detail}) {
            console.log('assets progress', detail.value);
        },
        handleAssetsLoaded: function({detail}) {
            console.log('assets loaded', detail.value);
            this.setData({loaded: true});
        },
        handleRaf: function({detail}) {
            console.log('raf', detail.value);
        }
    }

})

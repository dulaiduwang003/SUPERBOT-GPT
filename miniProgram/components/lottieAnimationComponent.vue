<template>
  <view class="zui-animator">
    <!-- #ifdef MP-WEIXIN -->
    <canvas type="2d" :class="`wrapper ${lottieId}`" :style="style"></canvas>
    <!-- #endif -->
    <!-- #ifndef MP-WEIXIN -->
    <view ref="lottieContainer" class="wrapper" :style="style"></view>
    <!-- #endif -->
  </view>
</template>

<script>
// #ifdef MP-WEIXIN
import Lottie from "lottie-miniprogram";
// #endif
// #ifndef MP-WEIXIN
import Lottie from "lottie-web";
// #endif

/**
 *
 */
export default {
  name: "lottie-animation",

  props: {
    width: {
      type: [Number, String],
      default: 48,
    },

    height: {
      type: [Number, String],
      default: 48,
    },

    frames: {
      type: Object,
      required: true,
      default: undefined,
    },

    loop: {
      type: Boolean,
      default: true,
    },

    autoplay: {
      type: Boolean,
      default: true,
    },

    speed: {
      type: Number,
      default: 1,
    },
  },

  data() {
    return {
      lottieId: `lottie-${Math.floor(Math.random() * 99999999)}`,
    };
  },

  computed: {
    style() {
      const style = {
        "--zui-animator-width":
            typeof this.width === "number" ? `${this.width}rpx` : this.width,
        "--zui-animator-height":
            typeof this.height === "number" ? `${this.height}rpx` : this.height,
      };

      return Object.keys(style)
          .map((key) => `${key}:${style[key]}`)
          .join("; ");
    },
  },

  watch: {
    frames() {
      this.initialAni();
    },

    speed() {
      this.setSpeed(this.speed);
    },
  },

  mounted() {
    this.initialAni();
  },

  unmounted() {
    if (!this.lottie) return;

    this.lottie.stop();
    this.lottie.destroy();
  },

  methods: {
    async initialAni() {
      if (this.lottie) {
        this.lottie.destroy();
      }

      if (!this.frames) return;

      // #ifdef MP-WEIXIN
      this.createSelectorQuery()
          .select("." + this.lottieId)
          .node((res) => {
            const canvas = res.node;
            canvas.width = parseInt(this.width)
            canvas.height = parseInt(this.height)

            Lottie.setup(canvas);

            this.launchLottie({
              renderer: "canvas",
              rendererSettings: {
                context: canvas.getContext('2d'),
              }
            })
          })
          .exec();
      // #endif
      // #ifndef MP-WEIXIN
      this.launchLottie({
        renderer: "svg",
        container: this.$refs.lottieContainer.$el
      })
      // #endif
    },

    launchLottie(opts) {
      this.lottie = Lottie.loadAnimation({
        ...opts,
        loop: this.loop,
        autoplay: this.autoplay,
        animationData: this.frames,
      });
      this.lottie.setSpeed(this.speed);
    },

    play() {
      if (!this.lottie) return;

      this.lottie.play();
    },

    stop() {
      if (!this.lottie) return;

      this.lottie.stop();
    },

    pause() {
      if (!this.lottie) return;

      this.lottie.pause();
    },

    setLocationHref(locationHref) {
      if (!this.lottie) return;

      this.lottie.setLocationHref(locationHref);
    },

    setSpeed(speed) {
      if (!this.lottie) return;

      this.lottie.setSpeed(speed);
    },

    gotoAndStop(value, isFrame) {
      if (!this.lottie) return;

      this.lottie.goToAndStop(value, isFrame);
    },

    gotoAndPlay(value, isFrame) {
      if (!this.lottie) return;

      this.lottie.goToAndPlay(value, isFrame);
    },

    setDirection(direction) {
      if (!this.lottie) return;

      this.lottie.setDirection(direction);
    },

    playSegments(segments, forceFlag) {
      if (!this.lottie) return;

      this.lottie.playSegments(segments, forceFlag);
    },

    setSubframe(flag) {
      if (!this.lottie) return;

      this.lottie.setSubframe(flag);
    },
  },
};
</script>

<style scoped lang="scss">
.zui-animator {
  display: inline-block;

  > .wrapper {
    width: var(--zui-animator-width);
    height: var(--zui-animator-height);
  }
}
</style>

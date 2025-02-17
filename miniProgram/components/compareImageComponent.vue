<template>
	<view class="imgComparison" :style="'width:' + width + 'rpx;height:' + height + 'rpx'">
		<view class="before-image" :style="'width:' + x + 'rpx'">
			<view :style="'width:' + width + 'rpx;height:' + height + 'rpx'">
				<image :src="beforeImageUrl" class="images" mode="aspectFill" data-type="before">
				</image>
			</view>
			<view v-if="beforeText" class="before-text">
				{{beforeText}}
			</view>
		</view>
		<view class="after-image">
			<view :style="'width:' + width + 'rpx;height:' + height + 'rpx'">
				<image :src="afterImageUrl" class="images" mode="aspectFill" data-type="after" >
				</image>
			</view>
			<view v-if="afterText" class="after-text">
				{{afterText}}
			</view>
		</view>
		<view class="slider-bar" :style="'left:' + x + 'rpx'" @touchmove="handleTouchMove"
			@touchstart="handleTouchStart" @touchend="handleTouchEnd">
			<view class="slider-button"></view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			width: {
				type: String,
				default: "750"
			},
			height: {
				type: String,
				default: "850"
			},
			beforeImageUrl: {
				type: String
			},
			afterImageUrl: {
				type: String
			},
			beforeText: {
				type: String,
				default: "参考图"
			},
			afterText: {
				type: String,
				default: "生成"
			}
		},

		data() {
			return {
				isPressDown: false, //鼠标是否正按住
				x: this.width / 2 //设置位置
			}
		},
		methods: {
			handleTouchStart(event) {
				this.isPressDown = true;
				this.x = event.target.offsetLeft * 2;
				if (this.x <= 0) {
					this.x = this.width / 2;
				}
			},
			handleTouchMove(event) {
				if (!this.isPressDown) {
					return;
				}
				this.x = event.touches[0].clientX * 2;
				if (this.x >= this.width) {
					this.x = this.width;
				} else if (this.x <= 0) {
					this.x = 0;
				}
			},
			handleTouchEnd(event) {
				this.isPressDown = false;
				return false;
			}
		}
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
	.imgComparison {
		position: relative;
		display: inline-block;
		overflow: hidden;

		.before-image {
			position: absolute;
			top: 0;
			left: 0;
			z-index: 1;
			width: 50%;
			overflow: hidden;

			.before-text {
				position: absolute;
				left: 10rpx;
				bottom: 10rpx;
				color: #FFFFFF;
				background-color: #2D2D2D;
				padding: 2rpx 8rpx 4rpx 6rpx;
				border-radius: 8rpx;
				font-size: 24rpx;
				white-space: nowrap;
			}
		}

		.after-image {
			position: absolute;
			top: 0;
			left: 0;
			z-index: 0;

			.after-text {
				position: absolute;
				right: 10rpx;
				bottom: 10rpx;
				color: #FFFFFF;
				background-color: #2D2D2D;
				padding: 2rpx 8rpx 4rpx 6rpx;
				border-radius: 8rpx;
				font-size: 24rpx;
				white-space: nowrap;
			}
		}

		.images {
			width: 100%;
			height: 100%;
		}

		.slider-bar {
			cursor: move;
			position: absolute;
			width: 4rpx;
			left: 50%;
			top: 0rpx;
			background: #FFFFFF;
			height: 100%;
			display: block;
			z-index: 2;
			box-shadow: 0 0 20rpx 1px rgba(0, 0, 0, 0.4);

			.slider-button {
				position: absolute;
				width: 60rpx;
				height: 60rpx;
				border-radius: 50%;
				border: 4rpx solid #FFFFFF;
				background-color: transparent;
				left: calc(50% - 30rpx);
				top: calc(50% - 30rpx);
			}

			&::before {
				content: '';
				padding: 6rpx;
				border: solid #FFFFFF;
				border-width: 0 4rpx 4rpx 0;
				position: absolute;
				top: calc(50% - 8rpx);
				left: calc(50% - 16rpx);
				transform: rotate(135deg);
				z-index: 1;
			}

			&::after {
				content: '';
				padding: 6rpx;
				border: solid #FFFFFF;
				border-width: 0 4rpx 4rpx 0;
				position: absolute;
				top: calc(50% - 8rpx);
				left: 50%;
				transform: rotate(-45deg);
			}
		}
	}
</style>

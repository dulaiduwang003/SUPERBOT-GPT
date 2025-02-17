<template>
	<view class="waterfalls-flow">
		<view v-for="(item,index) in data.column" :key="index" class="waterfalls-flow-column" :id="`waterfalls_flow_column_${index+1}`" :msg="msg" :style="{'width':w,'margin-left':index==0?0:m}">
			<view :class="['column-value',{'column-value-show':item2.o}]" v-for="(item2,index2) in columnValue(index)" :key="index2" :style="[s1]" @click.stop="wapperClick(item2)">
				<view class="inner" v-if="data.seat==1">
					<!-- #ifdef MP-WEIXIN -->
					<!-- #ifdef VUE2 -->
					<slot name="slot{{item2.index}}"></slot>
					<!-- #endif -->
					<!-- #ifdef VUE3 -->
					<slot :name="`slot${item2.index}`"></slot>
					<!-- #endif -->
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<slot v-bind="item2"></slot>
					<!-- #endif -->
				</view>
				<image :class="['img',{'img-hide':item2[hideImageKey]==true||item2[hideImageKey]==1},{'img-error':!item2[data.imageKey]}]" :src="item2[data.imageKey]" mode="widthFix" @load="imgLoad(item2,index+1)" @error="imgError(item2,index+1)" @click.stop="imageClick(item2)"></image>
				<view class="inner" v-if="data.seat==2">
					<!-- #ifdef MP-WEIXIN -->
					<!-- #ifdef VUE2 -->
					<slot name="slot{{item2.index}}"></slot>
					<!-- #endif -->
					<!-- #ifdef VUE3 -->
					<slot :name="`slot${item2.index}`"></slot>
					<!-- #endif -->
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<slot v-bind="item2"></slot>
					<!-- #endif -->
				</view>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		props: {
			value: Array,
			column: { // 列的数量
				type: [String, Number],
				default: 2
			},
			maxColumn: { // 最大列数
				type: [String, Number],
				default: 5
			},
			columnSpace: { // 列之间的间距 百分比
				type: [String, Number],
				default: 2
			},
			imageKey: { // 图片key
				type: [String],
				default: 'image'
			},
			hideImageKey: { // 隐藏图片key
				type: [String],
				default: 'hide'
			},
			seat: { // 文本的位置，1图片之上 2图片之下
				type: [String, Number],
				default: 2
			},
			listStyle: { // 单个展示项的样式：eg:{'background':'red'}
				type: Object
			}
		},
		data() {
			return {
				data: {
					list: this.value ? this.value : [],
					column: this.column < 2 ? 2 : this.column,
					columnSpace: this.columnSpace <= 5 ? this.columnSpace : 5,
					imageKey: this.imageKey,
					seat: this.seat
				},
				msg: 0,
				listInitStyle: {
					'border-radius': '15rpx',
					'margin-bottom': '20rpx',
					'background-color': '#161817'
				},
				adds: [], //预置数据
				isLoaded: true,
				curIndex: 0,
				isRefresh: true,
				flag: false,
				refreshDatas: []
			}
		},
		computed: {
			// 计算列宽
			w() {
				const column_rate = `${100 / this.data.column - (+this.data.columnSpace)}%`;
				return column_rate;
			},
			// 计算margin
			m() {
				const column_margin = `${(100-(100 / this.data.column - (+this.data.columnSpace)).toFixed(5)*this.data.column)/(this.data.column-1)}%`;
				return column_margin;
			},
			// list样式
			s1() {
				return { ...this.listInitStyle, ...this.listStyle };
			}
		},
		created() {
			// 初始化
			this.refresh();
		},
		methods: {
			// 预加载图片
			loadImages(idx = 0) {
				let count = 0;
				const newList = this.data.list.filter((item, index) => index >= idx);
				for (let i = 0; i < newList.length; i++) {
					// #ifndef APP-PLUS
					uni.getImageInfo({
						src: `${newList[i][this.imageKey]}.jpg`,
						complete: res => {
							count++;
							if (count == newList.length) this.initValue(idx);
						}
					})
					// #endif
					// #ifdef APP-PLUS
					plus.io.getImageInfo({
						src: `${newList[i][this.imageKey]}.jpg`,
						complete: res => {
							count++;
							if (count == newList.length) this.initValue(idx);
						}
					})
					// #endif
				}
			},
			// 刷新
			refresh() {
				if (!this.isLoaded) {
					this.refreshDatas = this.value;
					return false;
				};
				setTimeout(() => {
					this.refreshDatas = [];
					this.isRefresh = true;
					this.adds = [];
					this.data.list = this.value ? this.value : [];
					this.data.column = this.column < 2 ? 2 : this.column >= this.maxColumn ? this.maxColumn : this.column;
					this.data.columnSpace = this.columnSpace <= 5 ? this.columnSpace : 5;
					this.data.imageKey = this.imageKey;
					this.data.seat = this.seat;
					this.curIndex = 0;
					// 每列的数据初始化
					for (let i = 1; i <= this.data.column; i++) {
						this.data[`column_${i}_values`] = [];
						this.msg++;
					}
					this.$nextTick(() => {
						this.initValue(this.curIndex, 'refresh==>');
					})
				}, 1)
			},
			columnValue(index) {
				return this.data[`column_${index+1}_values`];
			},
			change(newValue) {
				for (let i = 0; i < this.data.list.length; i++) {
					const cv = this.data[`column_${this.data.list[i].column}_values`];
					for (let j = 0; j < cv.length; j++) {
						if (newValue[i] && i === cv[j].index) {
							this.data[`column_${this.data.list[i].column}_values`][j] = Object.assign(cv[j], newValue[i]);
							this.msg++;
							break;
						}
					}
				}
			},
			getMin(a, s) {
				let m = a[0][s];
				let mo = a[0];
				for (var i = a.length - 1; i >= 0; i--) {
					if (a[i][s] < m) {
						m = a[i][s];
					}
				}
				mo = a.filter(i => i[s] == m);
				return mo[0];
			},
			// 计算每列的高度
			getMinColumnHeight() {
				return new Promise(resolve => {
					const heightArr = [];
					for (let i = 1; i <= this.data.column; i++) {
						const query = uni.createSelectorQuery().in(this);
						query.select(`#waterfalls_flow_column_${i}`).boundingClientRect(data => {
							heightArr.push({ column: i, height: data.height });
						}).exec(() => {
							if (this.data.column <= heightArr.length) {
								resolve(this.getMin(heightArr, 'height'));
							}
						});
					}
				})
			},
			async initValue(i, from) {
				this.isLoaded = false;
				if (i >= this.data.list.length || this.refreshDatas.length) {
					this.msg++;
					this.loaded();
					return false;
				}
				const minHeightRes = await this.getMinColumnHeight();
				const c = this.data[`column_${minHeightRes.column}_values`];
				this.data.list[i].column = minHeightRes.column;
				c.push({ ...this.data.list[i], cIndex: c.length, index: i, o: 0 });
				this.msg++;
			},
			// 图片加载完成
			imgLoad(item, c) {
				const i = item.index;
				item.o = 1;
				this.$set(this.data[`column_${c}_values`], item.cIndex, JSON.parse(JSON.stringify(item)));
				this.initValue(i + 1);
			},
			// 图片加载失败
			imgError(item, c) {
				const i = item.index;
				item.o = 1;
				item[this.data.imageKey] = null;
				this.$set(this.data[`column_${c}_values`], item.cIndex, JSON.parse(JSON.stringify(item)));
				this.initValue(i + 1);
			},
			// 渲染结束
			loaded() {
				if (this.refreshDatas.length) {
					this.isLoaded = true;
					this.refresh();
					return false;
				}
				this.curIndex = this.data.list.length;
				if (this.adds.length) {
					this.data.list = this.adds[0];
					this.adds.splice(0, 1);
					this.initValue(this.curIndex);
				} else {
					if (this.data.list.length) this.$emit('loaded');
					this.isLoaded = true;
					this.isRefresh = false;
				}
			},
			// 单项点击事件
			wapperClick(item) {
				this.$emit('wapperClick', item);
			},
			// 图片点击事件
			imageClick(item) {
				this.$emit('imageClick', item);
			}
		},
		watch: {
			value: {
				deep: true,
				handler(newValue, oldValue) {
					setTimeout(() => {
						this.$nextTick(() => {
							if (this.isRefresh) return false;
							if (this.isLoaded) {
								// if (newValue.length <= this.curIndex) return this.refresh();
								if (newValue.length <= this.curIndex) return this.change(newValue);
								this.data.list = newValue;
								this.$nextTick(() => {
									this.initValue(this.curIndex, 'watch==>');
								})
							} else {
								this.adds.push(newValue);
							}
						})
					}, 10)
				}
			},
			column(newValue) {
				this.refresh();
			}
		}
	}
</script>
<style lang="scss" scoped>
	.waterfalls-flow {
		overflow: hidden;

		&-column {
			float: left;
		}
	}

	.column-value {
		width: 100%;
		font-size: 0;
		overflow: hidden;
		transition: opacity .4s;
		opacity: 0;

		&-show {
			opacity: 1;
		}

		.inner {
			font-size: 30rpx;
		}

		.img {
			width: 100%;

			&-hide {
				display: none;
			}

			&-error {
				background: #f2f2f2 url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAC4AAAAiAQMAAAAatXkPAAAABlBMVEUAAADMzMzIT8AyAAAAAXRSTlMAQObYZgAAAIZJREFUCNdlzjEKwkAUBNAfEGyCuYBkLyLuxRYW2SKlV1JSeA2tUiZg4YrLjv9PGsHqNTPMSAQuyAJgRDHSyvBPwtZoSJXakeJI9iuRLGDygdl6V0yKDtyMAeMPZySj8yfD+UapvRPj2JOwkyAooSV5IwdDjPdCPspe8LyTl9IKJvDETKKRv6vnlUasgg0fAAAAAElFTkSuQmCC) no-repeat center center;
			}
		}
	}
</style>

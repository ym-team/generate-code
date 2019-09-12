layui.config({
	base: 'js/'
}).use(['element', 'layer', 'navbar', 'tab'], function() {
	var element = layui.element()
	$ = layui.jquery,
		layer = layui.layer,
		navbar = layui.navbar(),
		tab = layui.tab({
			elem: '.layout-nav-card', //设置选项卡容器
			contextMenu:true
		});

	//iframe自适应
	$(window).on('resize', function() {
		var $content = $('.layout-nav-card .layui-tab-content');
		$content.height($(this).height() - 165);
		$content.find('iframe').each(function() {
			$(this).height($content.height());
		});
	}).resize();

	//模拟点击内容管理
	$('.beg-layout-menu').find('a[data-module-id=1]').click();

	element.on('nav(user)', function(data) {
		var $a = data.children('a');
		if($a.data('tab') !== undefined && $a.data('tab')) {
			tab.tabAdd({
				title: $a.children('cite').text(),
				//icon: 'fa-user',
				href: $a.data('url')
			});
		}
	});

	$('.beg-layout-side-toggle').on('click', function() {
		var sideWidth = $('.beg-layout-side').width();
		if(sideWidth === 200) {
			$('.beg-layout-body').animate({
				left: '0'
			});
			$('.beg-layout-footer').animate({
				left: '0'
			});
			$('.beg-layout-side').animate({
				width: '0'
			});
		} else {
			$('.beg-layout-body').animate({
				left: '200px'
			});
			$('.beg-layout-footer').animate({
				left: '200px'
			});
			$('.beg-layout-side').animate({
				width: '200px'
			});
		}
	});
});
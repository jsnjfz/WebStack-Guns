/*!
 * nav.js —— WebStack-Guns 前台首页交互脚本
 * 纯原生 JS，无依赖（不用 jQuery/构建工具/import-export）。
 * 以 <script src="/static/js/nav.js" defer> 加载，DOM 已就绪，无需再包 DOMContentLoaded。
 *
 * 接管原内联逻辑：
 *   1 深色模式读写（需与 head 防闪脚本结果一致） 2 搜索筛选(新增)
 *   3 侧边栏死链修复（原 $(href).offset() 遇不存在锚点会抛错，如线上 #test/#灵感采集）
 *   4 平滑滚动（原 $("a.smooth").click(...)） 5 滚动高亮 scrollspy(新增) 6 页脚年份
 *   7 站点图标多级兜底(新增，替代每张卡片内联 onerror)
 *   8 卡片补充域名(新增) 9 侧边栏分类计数(新增)
 *
 * 各 initXxx 模块独立、各自 try/catch，互不影响；页面缺元素时静默跳过。
 */
(function () {
  'use strict';

  var THEME_KEY = 'nav-theme';

  // 取出 href 的 hash 并 decode（中文锚点属性里可能是 %E7%81%B5... 编码形式）
  function getHashId(href) {
    var i = (href || '').indexOf('#');
    if (i === -1) return '';
    var raw = href.slice(i + 1);
    if (!raw) return '';
    try {
      return decodeURIComponent(raw);
    } catch (e) {
      return raw;
    }
  }

  /* ===================== 1. 深色模式 ===================== */
  function initTheme() {
    var root = document.documentElement;
    var mql = window.matchMedia ? window.matchMedia('(prefers-color-scheme: dark)') : null;

    function saved() {
      try {
        return localStorage.getItem(THEME_KEY);
      } catch (e) {
        return null;
      }
    }

    function apply(theme) {
      root.setAttribute('data-theme', theme);
      var btn = document.getElementById('nav-theme-toggle');
      if (!btn) return;
      var icon = btn.querySelector('i');
      if (icon) {
        icon.classList.remove('fa-moon-o', 'fa-sun-o');
        icon.classList.add(theme === 'dark' ? 'fa-sun-o' : 'fa-moon-o');
      }
      btn.setAttribute('aria-label', theme === 'dark' ? '切换浅色模式' : '切换深色模式');
    }

    // 优先级：localStorage > 系统 prefers-color-scheme > 亮色，需与 head 防闪脚本一致
    var s = saved();
    apply(s === 'light' || s === 'dark' ? s : ((mql && mql.matches) ? 'dark' : 'light'));

    var toggleBtn = document.getElementById('nav-theme-toggle');
    if (toggleBtn) {
      toggleBtn.addEventListener('click', function () {
        var next = root.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
        apply(next);
        try {
          localStorage.setItem(THEME_KEY, next);
        } catch (e) { /* 忽略 */ }
      });
    }

    if (mql) {
      var onSystemChange = function (e) {
        if (saved() === 'light' || saved() === 'dark') return; // 用户手动设置过不跟随系统
        apply(e.matches ? 'dark' : 'light');
      };
      if (typeof mql.addEventListener === 'function') mql.addEventListener('change', onSystemChange);
      else if (typeof mql.addListener === 'function') mql.addListener(onSystemChange);
    }
  }

  /* ===================== 2. 侧边栏死链修复 ===================== */
  // 部分 a.smooth 指向不存在的锚点，原 jQuery offset() 会抛错导致后续绑定全部失效。
  // 这里预先清理死链菜单项；若父级子菜单因此被清空，一并移除父级。
  function initSidebarDeadLinks() {
    var menu = document.getElementById('main-menu');
    if (!menu) return;

    Array.from(menu.querySelectorAll('a.smooth')).forEach(function (a) {
      var id = getHashId(a.getAttribute('href'));
      if (!id || document.getElementById(id)) return;

      var li = a.closest ? a.closest('li') : null;
      if (!li) return;

      console.warn('[nav.js] 侧边栏锚点不存在，已移除菜单项：#' + id);

      var parentUl = li.parentElement;
      li.remove();

      if (parentUl && parentUl !== menu && parentUl.tagName === 'UL' && parentUl.children.length === 0) {
        var parentLi = parentUl.parentElement;
        if (parentLi && parentLi.tagName === 'LI') parentLi.remove();
      }
    });
  }

  /* ===================== 3. 站点搜索 / 筛选 ===================== */
  function initSearch() {
    var input = document.getElementById('nav-search');
    var main = document.getElementById('nav-main');
    if (!input || !main) return; // 例如 about 页没有搜索框，静默跳过

    var clearBtn = document.getElementById('nav-search-clear');
    var noResult = document.getElementById('nav-no-result');
    var menu = document.getElementById('main-menu');

    // 卡片标题/描述/链接不放在 data-* 属性上（避免录入内容含双引号截断属性、
    // 避免重复注入文本），改为从卡片 DOM 内部读取；抽成函数便于以后替换取数方式。
    // 所属分类名也纳入检索：站点文本里往往不含分类词（例如没有任何一张卡片
    // 的标题或描述含「配色」，但存在「在线配色」分类），不带上会搜不出来。
    function extractCardText(colEl, sectionName) {
      var titleEl = colEl.querySelector('.xe-user-name');
      var descEl = colEl.querySelector('.xe-comment p');
      var linkEl = colEl.querySelector('a.nav-card');
      var title = titleEl ? (titleEl.textContent || '').trim() : '';
      var desc = descEl ? (descEl.textContent || '').trim() : '';
      var url = linkEl ? (linkEl.getAttribute('href') || '').trim() : '';
      return (title + ' ' + desc + ' ' + url + ' ' + (sectionName || '')).toLowerCase();
    }

    var sections = Array.from(main.querySelectorAll('.nav-section')).map(function (sectionEl) {
      var titleEl = sectionEl.querySelector('.nav-section-title');
      var sectionName = sectionEl.getAttribute('data-section') ||
        (titleEl ? (titleEl.textContent || '').trim() : '');
      var cols = Array.from(sectionEl.querySelectorAll('.nav-col')).map(function (colEl) {
        return { el: colEl, text: extractCardText(colEl, sectionName) };
      });
      return { el: sectionEl, name: sectionName, cols: cols };
    });

    // 检索结果条：命中数 + 按分类收窄的 chip。仅在检索态出现，
    // 浏览态不占位，避免和侧边栏的分类导航重复。
    var bar = document.createElement('div');
    bar.id = 'nav-results-bar';
    bar.className = 'nav-results-bar';
    bar.hidden = true;
    bar.innerHTML = '<p class="nav-results-count" role="status" aria-live="polite"></p>' +
                    '<div class="nav-results-chips"></div>';
    var barCount = bar.querySelector('.nav-results-count');
    var barChips = bar.querySelector('.nav-results-chips');
    var pageTitle = main.querySelector('.nav-page-title');
    if (pageTitle && pageTitle.nextSibling) {
      main.insertBefore(bar, pageTitle.nextSibling);
    } else {
      main.insertBefore(bar, main.firstChild);
    }

    // 当前按分类收窄的选择；空串表示「全部」
    var activeCat = '';

    // chip 用事件委托，避免每次重建后重新绑定
    barChips.addEventListener('click', function (e) {
      var btn = e.target.closest ? e.target.closest('.nav-chip') : null;
      if (!btn) return;
      activeCat = btn.getAttribute('data-cat') || '';
      applyFilter(true); // 保留 chip 列表，只切换收窄条件
    });

    // 缓存侧边栏链接 -> 目标分区，供筛选时联动隐藏菜单项
    var sidebarLinks = menu ? Array.from(menu.querySelectorAll('a.smooth')).map(function (a) {
      var target = document.getElementById(getHashId(a.getAttribute('href')));
      return { li: a.closest ? a.closest('li') : null, sectionEl: target ? target.closest('.nav-section') : null };
    }) : [];

    var debounceTimer = null;

    function renderChips(hits, total) {
      // 只命中一个分类时整排 chip 都没有意义（只会剩一个「全部」），直接不渲染
      if (hits.length <= 1) {
        barChips.innerHTML = '';
        return;
      }
      // hits: [{name, n}]，按命中数降序；只列出真正命中的分类
      var html = '<button type="button" class="nav-chip' + (activeCat ? '' : ' is-active') +
                 '" data-cat="">全部 <span class="nav-chip-n">' + total + '</span></button>';
      hits.forEach(function (h) {
        var on = activeCat === h.name;
        html += '<button type="button" class="nav-chip' + (on ? ' is-active' : '') +
                '" data-cat="' + h.name.replace(/"/g, '&quot;') + '">' +
                h.name.replace(/[<>&]/g, '') + ' <span class="nav-chip-n">' + h.n + '</span></button>';
      });
      barChips.innerHTML = html;
    }

    /**
     * @param {boolean} keepChips true 表示本次只是切换分类 chip，
     *        关键词未变，不重建 chip 列表（否则点一下 chip 列表就只剩它自己）
     */
    function applyFilter(keepChips) {
      var raw = input.value.trim().toLowerCase();
      var words = raw ? raw.split(/\s+/) : [];
      var searching = words.length > 0;
      if (!searching) activeCat = ''; // 退出检索态时清掉分类收窄

      var hits = [];      // 关键词命中的分类分布（不受 chip 影响）
      var shown = 0;      // 最终可见卡片数（受 chip 收窄影响）

      sections.forEach(function (section) {
        var hitInSection = 0;
        var shownInSection = 0;
        var catOk = !activeCat || section.name === activeCat;

        section.cols.forEach(function (col) {
          var match = !searching || words.every(function (w) { return col.text.indexOf(w) !== -1; });
          if (match) hitInSection++;
          var visible = match && catOk;
          col.el.classList.toggle('nav-hidden', !visible);
          if (visible) shownInSection++;
        });

        if (hitInSection > 0) hits.push({ name: section.name, n: hitInSection });
        shown += shownInSection;
        // 检索态下分区容器本身被 display:contents 消融，隐藏与否只影响浏览态
        section.el.classList.toggle('nav-hidden', shownInSection === 0);
      });

      // 检索态：正文切换为扁平结果网格（CSS 侧用 .nav-searching 驱动）
      main.classList.toggle('nav-searching', searching);
      main.dataset.navSearching = searching ? '1' : ''; // 供 scrollspy 判断是否暂停

      if (searching) {
        hits.sort(function (a, b) { return b.n - a.n; });
        var total = hits.reduce(function (a, b) { return a + b.n; }, 0);
        barCount.textContent = total > 0
          ? '找到 ' + total + ' 个站点' + (activeCat ? '，已筛选「' + activeCat + '」' + shown + ' 个' : '')
          : '没有匹配的站点';
        if (!keepChips) renderChips(hits, total);
        barChips.hidden = barChips.children.length === 0;
        bar.hidden = false;
      } else {
        bar.hidden = true;
        barChips.innerHTML = '';
      }

      // 检索态下侧边栏分类与结果对不上，联动隐藏未命中的项
      sidebarLinks.forEach(function (link) {
        if (!link.li) return;
        var hidden = searching && link.sectionEl ? link.sectionEl.classList.contains('nav-hidden') : false;
        link.li.classList.toggle('nav-hidden', hidden);
      });

      if (noResult) noResult.hidden = shown > 0;
      if (clearBtn) clearBtn.hidden = raw.length === 0;
    }

    function scheduleFilter() {
      if (debounceTimer) clearTimeout(debounceTimer);
      // 显式包一层：关键词变化时必须重建 chip，不能把 setTimeout 的回调参数
      // 当成 keepChips 传进去
      debounceTimer = setTimeout(function () { applyFilter(false); }, 120);
    }

    function resetSearch() {
      input.value = '';
      applyFilter();
    }

    input.addEventListener('input', scheduleFilter);

    input.addEventListener('keydown', function (e) {
      if (e.key === 'Escape') resetSearch();
    });

    if (clearBtn) {
      clearBtn.addEventListener('click', function () {
        resetSearch();
        input.focus();
      });
    }

    // / 或 Ctrl/Cmd+K 聚焦搜索框，焦点已在输入类元素中时不拦截
    document.addEventListener('keydown', function (e) {
      var t = e.target;
      var isEditable = !!t && (t.tagName === 'INPUT' || t.tagName === 'TEXTAREA' || t.isContentEditable);
      if (isEditable) return;

      if (e.key === '/' || ((e.ctrlKey || e.metaKey) && (e.key === 'k' || e.key === 'K'))) {
        e.preventDefault();
        input.focus();
      }
    });

    applyFilter(); // 初始化一次，确保按钮/提示状态正确
  }

  /* ===================== 4. 平滑滚动 ===================== */
  function initSmoothScroll() {
    var menu = document.getElementById('main-menu');
    if (!menu) return;

    var reduceMotion = !!(window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches);
    var OFFSET = 24; // 顶部预留余量，避免标题被顶栏遮挡

    menu.addEventListener('click', function (e) {
      var a = e.target && e.target.closest ? e.target.closest('a.smooth') : null;
      if (!a || !menu.contains(a)) return;

      var id = getHashId(a.getAttribute('href'));
      var target = id ? document.getElementById(id) : null;
      if (!target) return; // 目标不存在时安全跳过，不抛错

      e.preventDefault();

      var top = target.getBoundingClientRect().top + window.pageYOffset - OFFSET;
      window.scrollTo({ top: top, behavior: reduceMotion ? 'auto' : 'smooth' });

      if (window.history && history.replaceState) {
        history.replaceState(null, '', '#' + encodeURIComponent(id));
      }

      // 窄屏下点击后收起移动端菜单
      var sidebar = document.querySelector('.sidebar-menu');
      if (sidebar) sidebar.classList.remove('mobile-is-visible');
      menu.classList.remove('mobile-is-visible');
    });
  }

  /* ===================== 5. 滚动高亮 scrollspy ===================== */
  function initScrollSpy() {
    var main = document.getElementById('nav-main');
    var menu = document.getElementById('main-menu');
    if (!main || !menu || !('IntersectionObserver' in window)) return;

    var titles = Array.from(main.querySelectorAll('.nav-section-title'));
    if (!titles.length) return;

    // 分区 id -> 侧边栏 <li> 映射
    var linkMap = {};
    Array.from(menu.querySelectorAll('a.smooth')).forEach(function (a) {
      var id = getHashId(a.getAttribute('href'));
      var li = a.closest ? a.closest('li') : null;
      if (id && li) linkMap[id] = li;
    });

    var allLis = Array.from(menu.querySelectorAll('li'));

    function setActive(id) {
      allLis.forEach(function (li) { li.classList.remove('active'); });

      var li = linkMap[id];
      if (!li) return;
      li.classList.add('active');

      // 父级菜单同步高亮并展开
      var node = li.parentElement;
      while (node && node !== menu && node.tagName === 'UL') {
        var parentLi = node.parentElement;
        if (!parentLi || parentLi.tagName !== 'LI') break;
        parentLi.classList.add('active');
        parentLi.classList.add('expanded');
        node = parentLi.parentElement;
      }
    }

    var visible = new Map();

    var observer = new IntersectionObserver(function (entries) {
      if (main.dataset.navSearching) return; // 搜索中暂停高亮，避免乱跳

      entries.forEach(function (entry) {
        if (entry.isIntersecting) visible.set(entry.target, entry.boundingClientRect.top);
        else visible.delete(entry.target);
      });
      if (visible.size === 0) return;

      var topMost = null;
      var topMostPos = Infinity;
      visible.forEach(function (pos, el) {
        if (pos < topMostPos) {
          topMostPos = pos;
          topMost = el;
        }
      });
      if (topMost && topMost.id) setActive(topMost.id);
    }, { root: null, rootMargin: '-72px 0px -70% 0px', threshold: 0 });

    titles.forEach(function (title) { observer.observe(title); });
  }

  /* ===================== 6. 页脚年份 ===================== */
  function initFooterYear() {
    var el = document.getElementById('nav-year');
    if (el) el.textContent = String(new Date().getFullYear());
  }

  /* ===================== 启动 ===================== */
  /* ===================== 8. 卡片补充域名 ===================== */
  // 实测 207 条描述里有 67% 超过两行被截断、22 条与标题互相包含，信息价值低；
  // 而 207/207 都能解析出域名。对导航站来说 dribbble.com 比「全球UI设计师
  // 作品分享平台」更好认，故在标题行补一个域名，描述压到一行。
  function initCardDomain() {
    var cards = document.querySelectorAll('a.nav-card');
    if (!cards.length) return;

    Array.prototype.forEach.call(cards, function (card) {
      var descEl = card.querySelector('.xe-comment p');
      if (!descEl || descEl.querySelector('.nav-card-domain')) return;

      var host = '';
      try {
        host = new URL(card.href, window.location.origin).hostname.replace(/^www\./, '');
      } catch (e) {
        return; // 解析不出来就保持原样，不插入空元素
      }
      if (!host) return;

      // 域名放在描述行开头而不是标题行：标题行并排时两者会互相挤压，
      // 出现「Freebie Su… freebiesupply…」这种双双截断，反而不如只显示标题。
      // 放进 <p> 内部可直接复用它的单行省略，无需额外包裹层。
      var span = document.createElement('span');
      span.className = 'nav-card-domain';
      span.textContent = host;
      descEl.insertBefore(span, descEl.firstChild);

      // 完整描述可能被一行截断，挂到 title 上便于悬停查看
      var desc = (descEl.textContent || '').replace(host, '').trim();
      if (desc && !card.title) card.title = desc;
    });
  }

  /* ===================== 9. 侧边栏分类计数 ===================== */
  // 25 个分类外观完全一致，但体量差 24 倍（摄影图库 24 个 vs 媒体导航 1 个），
  // 点进去才知道有多少。这里在菜单项右侧补一个计数。
  function initSidebarCounts() {
    var menu = document.getElementById('main-menu');
    if (!menu) return;

    Array.prototype.forEach.call(menu.querySelectorAll('a.smooth'), function (a) {
      if (a.querySelector('.nav-menu-count')) return;
      var target = document.getElementById(getHashId(a.getAttribute('href')));
      var section = target ? target.closest('.nav-section') : null;
      if (!section) return;
      var n = section.querySelectorAll('.nav-col').length;
      if (!n) return;

      var badge = document.createElement('span');
      badge.className = 'nav-menu-count';
      badge.textContent = n;
      badge.setAttribute('aria-hidden', 'true'); // 纯视觉提示，避免读屏念成菜单名的一部分
      a.appendChild(badge);
    });
  }

  /* ===================== 7. 站点图标兜底 ===================== */
  // 图标优先走 /static/tmp/（随仓库发布、可缓存、无重定向），取不到再退回
  // /kaptcha/（后台新上传的图只在上传目录里），最后退到本地占位图。
  // 放在 JS 里而不是每个 <img> 内联 onerror：196 张卡片可省下约 17KB HTML。
  var ICON_STATIC = '/static/tmp/';
  var ICON_UPLOAD = '/kaptcha/';
  var ICON_PLACEHOLDER = '/static/img/github.png';

  function nextIconSrc(img) {
    var src = img.getAttribute('src') || '';
    var name = src.slice(src.lastIndexOf('/') + 1);
    if (!name) return null;
    if (src.indexOf(ICON_STATIC) !== -1) return ICON_UPLOAD + name;
    if (src.indexOf(ICON_UPLOAD) !== -1) return ICON_PLACEHOLDER;
    return null; // 已是占位图或站外图（如 about 页头像自带 onerror），不再接管
  }

  function fallbackIcon(img) {
    if (!img || img.tagName !== 'IMG' || !img.classList.contains('nav-card-icon')) return;
    var next = nextIconSrc(img);
    if (next) img.setAttribute('src', next);
  }

  function initIconFallback() {
    // error 事件不冒泡，只能在捕获阶段监听
    window.addEventListener('error', function (e) {
      fallbackIcon(e.target);
    }, true);

    // 兜底扫描：defer 脚本执行前可能已有图片加载失败，错过了上面的监听
    window.addEventListener('load', function () {
      Array.prototype.forEach.call(
        document.querySelectorAll('img.nav-card-icon'),
        function (img) {
          if (img.complete && img.naturalWidth === 0) fallbackIcon(img);
        }
      );
    });
  }

  function safeRun(fn) {
    try {
      fn();
    } catch (err) {
      if (window.console && console.error) console.error('[nav.js] 模块执行出错：' + (fn && fn.name), err);
    }
  }

  function boot() {
    safeRun(initIconFallback); // 需最早注册，尽量赶在图片加载失败之前
    safeRun(initTheme);
    safeRun(initSidebarDeadLinks);   // 先清死链
    safeRun(initSidebarCounts);      // 再对剩下的菜单项计数
    safeRun(initCardDomain);         // 须早于 initSearch，使域名进入检索文本缓存
    safeRun(initSearch);
    safeRun(initSmoothScroll);
    safeRun(initScrollSpy);
    safeRun(initFooterYear);
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', boot);
  } else {
    boot();
  }

  window.NavUI = { version: '1.0.0' }; // 唯一暴露的全局命名空间
})();

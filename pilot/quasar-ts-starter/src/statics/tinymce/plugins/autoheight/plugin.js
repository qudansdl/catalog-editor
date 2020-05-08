/* eslint-disable */
/**
 *
 * Version: 5.2.2 (2020-04-23)
 */
(function () {
    'use strict';

    var Cell = function (initial) {
      var value = initial;
      var get = function () {
        return value;
      };
      var set = function (v) {
        value = v;
      };
      var clone = function () {
        return Cell(get());
      };
      return {
        get: get,
        set: set,
        clone: clone
      };
    };

    var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

    var global$1 = tinymce.util.Tools.resolve('tinymce.Env');

    var global$2 = tinymce.util.Tools.resolve('tinymce.util.Delay');

    var fireResizeEditor = function (editor) {
      return editor.fire('ResizeEditor');
    };
    var Events = { fireResizeEditor: fireResizeEditor };

    var getAutoHeightMaxHeight = function (editor) {
      return editor.getParam('max_height', 0, 'number');
    };
    var getAutoHeightOverflowPadding = function (editor) {
      return editor.getParam('autoheight_overflow_padding', 1, 'number');
    };

    var shouldAutoHeightOnInit = function (editor) {
      return editor.getParam('autoheight_on_init', true, 'boolean');
    };
    var Settings = {
      getAutoHeightMaxHeight: getAutoHeightMaxHeight,
      getAutoHeightOverflowPadding: getAutoHeightOverflowPadding,
      shouldAutoHeightOnInit: shouldAutoHeightOnInit
    };

    var isFullscreen = function (editor) {
      return editor.plugins.fullscreen && editor.plugins.fullscreen.isFullscreen();
    };
    var wait = function (editor, oldSize, times, interval, callback) {
      global$2.setEditorTimeout(editor, function () {
        resize(editor, oldSize);
        if (times--) {
          wait(editor, oldSize, times, interval, callback);
        } else if (callback) {
          callback();
        }
      }, interval);
    };
    var toggleScrolling = function (editor, state) {
      var body = editor.getBody();
      if (body) {
        body.style.overflowY = state ? '' : 'hidden';
        if (!state) {
          body.scrollTop = 0;
        }
      }
    };

    var resize = function (editor, oldSize) {
      var deltaSize, resizeHeight;
      var dom = editor.dom;
      var doc = editor.getDoc();
      if (!doc) {
        return;
      }
      if (isFullscreen(editor)) {
        toggleScrolling(editor, true);
        return;
      }
      resizeHeight = editor.getContainer().parentElement.offsetHeight;

      var maxHeight = Settings.getAutoHeightMaxHeight(editor);
      if (maxHeight && resizeHeight > maxHeight) {
        resizeHeight = maxHeight;
        toggleScrolling(editor, true);
      } else {
        toggleScrolling(editor, false);
      }
      if (resizeHeight !== oldSize.get()) {
        deltaSize = resizeHeight - oldSize.get();
        dom.setStyle(editor.getContainer(), 'height', resizeHeight + 'px');
        oldSize.set(resizeHeight);
        Events.fireResizeEditor(editor);
        if (global$1.browser.isSafari() && global$1.mac) {
          var win = editor.getWin();
          win.scrollTo(win.pageXOffset, win.pageYOffset);
        }
        if (editor.hasFocus()) {
          editor.selection.scrollIntoView(editor.selection.getNode());
        }
        if (global$1.webkit && deltaSize < 0) {
          resize(editor, oldSize);
        }
      }
    };
    var setup = function (editor, oldSize) {
      editor.on('init', function () {
        var overflowPadding = Settings.getAutoHeightOverflowPadding(editor);
        var dom = editor.dom;
        dom.setStyles(editor.getBody(), {
          'paddingLeft': overflowPadding,
          'paddingRight': overflowPadding,
          'min-height': 0
        });
      });
      editor.on('NodeChange SetContent keyup FullscreenStateChanged ResizeContent', function () {
        resize(editor, oldSize);
      });
      if (Settings.shouldAutoHeightOnInit(editor)) {
        editor.on('init', function () {
          wait(editor, oldSize, 20, 100, function () {
            wait(editor, oldSize, 5, 1000);
          });
        });
      }
    };
    var Resize = {
      setup: setup,
      resize: resize
    };

    var register = function (editor, oldSize) {
      editor.addCommand('mceAutoHeight', function () {
        Resize.resize(editor, oldSize);
      });
    };
    var Commands = { register: register };

    function Plugin () {
      global.add('autoheight', function (editor) {
        if (!editor.settings.hasOwnProperty('resize')) {
          editor.settings.resize = false;
        }
        if (!editor.inline) {
          var oldSize = Cell(0);
          Commands.register(editor, oldSize);
          Resize.setup(editor, oldSize);
        }
      });
    }

    Plugin();

}());

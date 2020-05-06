<template>
  <q-page padding>
    <div class="text-h3 q-my-lg">i18n</div>
    <div>Play with app internationalization</div>

    <div class="text-h4 q-mt-lg q-mb-sm">Detecting Locale</div>
    <div>Browser locale: {{ this.$q.lang.getLocale() }}</div>

    <div class="text-h4 q-mt-lg q-mb-sm">Change Quasar Language Pack at Runtime</div>
    <div>
      <q-select
        v-model="lang"
        :options="langOptions"
        label="Quasar Language"
        dense
        borderless
        emit-value
        map-options
        options-dense
        style="width: 150px"
      />
    </div>
    <div>
      "Close" label in current Quasar Language Pack is: {{ $q.lang.label.close }}
    </div>

    <div class="text-h4 q-mt-lg q-mb-sm">Use app's lang pack</div>
    <div>in HTML body: {{ $t('mykey1') }}</div>
    <div>in attribute:
      <q-btn color="primary" icon="check" :label="$t('mykey2')" />
    </div>
    <div>programmatically: <span v-html="content"></span></div>

    <div class="text-h4 q-mt-lg q-mb-sm">Other <a href="https://kazupon.github.io/vue-i18n" target="_blank">Vue I18n</a> feature</div>
    <div>
      <a href="https://kazupon.github.io/vue-i18n/guide/datetime.html" target="_blank">
        DateTime localization
      </a>: {{ $d(new Date(), 'short') }}
    </div>
    <div>
      <a href="https://kazupon.github.io/vue-i18n/guide/fallback.html" target="_blank">
        Fallback localization
      <!-- eslint-disable-next-line vue-i18n/no-missing-keys -->
      </a>: {{ $t('success') }}
    </div>
  </q-page>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import languages from 'quasar/lang/index.json';
import { Watch } from 'vue-property-decorator';

const appLanguages = languages.filter((lang) => ['de', 'en-us'].includes(lang.isoName));

@Component
export default class I18n extends Vue {
  lang = this.$q.lang.isoName;

  langOptions = appLanguages.map((l) => ({
    label: l.nativeName, value: l.isoName,
  }));

  get content() { return this.$t('mykey3'); }

  @Watch('lang')
  // eslint-disable-next-line class-methods-use-this
  langChange(lang: string) {
    import(/* webpackInclude: /(de|en-us)\.js$/ */ `quasar/lang/${lang}`).then(
      (l) => { this.$q.lang.set(l.default); },
    );
    this.$i18n.locale = lang;
    this.$q.cookies.set('lang', lang);
  }
}
</script>

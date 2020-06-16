import {
  GET_PATTERN_BY_ID,
  GET_PATTERNS
} from '@/api/graphql/query/pattern'
import {ICategoryData, IPatternData} from '@/api/types'
import Vue from 'vue';


export default class GraphqlApiPattern {


  private static listQuery = {
    start: 0,
    length: 10,
    order: [{
      column: 'created',
      dir: 'desc'
    }],
    columns: []
  }

  static getPattern = (patternId: string | null) => {
    console.log('get Pattern')
    return Vue.prototype.$apollo.query({
      query: GET_PATTERN_BY_ID,
      variables: {
        patternId
      }
    })
  };

  static getPatterns = (start: number, length: number, tags: ICategoryData[]) => {
    console.log('get Patterns')

    const query = JSON.parse(JSON.stringify(GraphqlApiPattern.listQuery))
    query.start = start
    query.length = length
    if (tags.length > 0) {
      query.columns.push({
        name: 'categories',
        operation: '',
        value: '',
        columns: [{
          name: 'id',
          operation: 'in',
          value: tags.map(c => c.id).join(',')
        }]
      })
    }
    return Vue.prototype.$apollo.query({
      query: GET_PATTERNS,
      variables: {
        query
      }
    })
  };
}

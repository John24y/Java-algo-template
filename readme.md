
## 简介

一个用于算法竞赛的Java模板仓库。

## 目录结构

- `src/main` 用于各平台的main类模板

- `src/template` 算法模板

## 模板LIST

### 数据结构

- 字典树 `Trie` 
- 平衡树-Treap `Treap` 

####  线段树
- 单点更新线段树 `SingleApplySegTree`
- 区间操作线段树 `LazySegTree`
- 可持久化线段树（静态数组任意区间第k大） `PersistSegTree`
- 树套树（动态数组任意区间第k大） `TwoDSegTree`

### 数学模板

- 组合数/排列数 `Comb`
- gcd `MathTemplate.gcd`
- 快速幂 `MathTemplate.ksm`
- 判断x是否素数 O(n^0.5) `MathTemplate.isPrime`
- 判断1-N的每个数是否素数 O(n^0.5) `MathTemplate.isPrimes`
- 查找1-N的所有素数 O(n^0.5) `MathTemplate.getPrimes`
- 素因数分解 O(logn) `MathTemplate.factors`
- 素因数分解并算重复次数 O(logn) `MathTemplate.factorsRepeat`

### 图论

- 最大二分图匹配（匈牙利算法） `BFSMatching` `DFSMatching`
- 有向图强联通分量/割点 `Tarjan`
- LCA `TreeLCA`
- 并查集 `UnionFind`
- 树链剖分 `TreePathDecomposition`
- 基环树找环 `RingTree`

### 字符串
- KMP
- 后缀数组
- Z函数
- 字符串双值哈希

### 其他
- 二分查找 `BinarySearch`
- 单调栈6个模板 `MonoStack`
  - 给定i，找a[j]>=a[i]的最小/最大下标j
  - 给定i，找a[j]>a[i]的最小/最大下标j
  - 给定i，找a[j]<=a[i]的最小/最大下标j
  - 给定i，找a[j]<a[i]的最小/最大下标j



package jp2018;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class HW4 {
	
	
	/**
	 * Generating a list with content v0,v1,.., vn, where
	 * v0 = from, v1 = from + step, v2 = from + 2*step, ..., vn= from + n * step, and
	 * n = |to-from| / step . Note that the list is increasing if from <= to and step > 0, and
	 * decreasing if from >= to and step < 0. In all other cases, you can return null;
	 * Ex: seq(1, 10, 3) returns list [1,4,7,10] while seq(10, 2,-3) returns [10, 7, 4]   
	 * @param from: initial number of the list.
	 * @param to : the bound of final element. 
	 * @param step : the increment/decrement of next element.
	 * @return a list  
	 */
	public static List<Integer> seq(int from, int to, int step) {

		if (from > to && step > 0 || from < to && step < 0 || step == 0)
			return null;		
		
		List<Integer> rlt = new ArrayList<>();
		// TO-DO: put you code here to handle other cases
		if(from < to) {
			while(from <= to) {
				rlt.add(from);
				from += step;
			}
		}
		else {
			while(from >= to) {
				rlt.add(from);
				from += step;
			}
		}
		
		return rlt;
	}
    
    /**
	 * Given a list lst of distinct positive integers, return the sublist consisting of all
	 * elements of lst which cannot divide other elements of lst.
	 * The elements of the returned list must preserve their
	 * order in the original list. Ex: if lst = [2,4,7,9,3,10], then
	 * the result is [4,7,9,10]. 2 and 3 are not in the result because they can divide
	 * 4 and 9, respectively.  
	 *    
	 * @param lst: a list of positive integers.
	 * @return a sublist of lst.  
	 */
	public static List<Integer> nfSublist(List<Integer> lst) {
		
		List<Integer> rlt = new ArrayList<>();
		// TO-DO: put you code here
		rlt.addAll(lst);
		int [] array = new int [rlt.size()];
		for(int i = 0; i < rlt.size(); i ++) {
			array[i] = 0;
		}
		
		for(int i = 0; i < rlt.size(); i ++) {
			for(int j = 0 ; j < rlt.size(); j ++) {
				if(i == j) continue;
				else {
					if(rlt.get(j) % rlt.get(i) == 0) {
						array[i] = 1;
						break;
					}
				}
			}
		}
		List<Integer> rlt_new = new ArrayList<>();
		
		for(int i = 0; i < rlt.size(); i ++) {
			if(array[i] == 0) {
				rlt_new.add(rlt.get(i));
			}
		}
		
		return rlt_new;
	}
	        
		
	
	/**
	 * The Fibonacci sequence 0,1,2,3,5,8,.... can be defined recursively as
	 * follows: f(0) = 0, f(1) = 1, and f(k) = f(k-1) + f(k-2) for k > 1. Write
	 * a method for returning f(n) on input n. Note that the method MUST be able
	 * to compute f(1000) in 5 seconds without exhausting available memory. As a
	 * result, the direct recursive translation of the definition as given in
	 * method f1 is infeasible. You can rewrite it without using recursion.
	 * 
	 * @param k
	 * @return
	 */
	public static BigInteger f(int k) {
		// put your code here!
		BigInteger[] fiboSeq = new BigInteger[1001];
		fiboSeq[0] = BigInteger.ZERO;
		fiboSeq[1] = BigInteger.ONE;
		for(int i = 2; i <= k; i ++) {
			fiboSeq[i] = fiboSeq[i-1].add(fiboSeq[i-2]);
		}
		return fiboSeq[k];
	}

	// This version can return value in time only for small input.
	public static BigInteger f1(int k) {
		if (k == 0)
			return BigInteger.ZERO;
		else if (k == 1)
			return BigInteger.ONE;
		else
			return f1(k - 1).add(f1(k - 2));
	}	
	
	
	
	/**
	 * Given a list lst of items [x_1,x_2,...,x_n] of type List<T>, return a
	 * Map<T, Integer> m such that m.get(x) = k > 0 iff x occurs in lst k times,
	 * and return null if x does not occur in lst. Ex: if lst = ["d", "c", "a",
	 * "c", "d"], then the return result m should have the property that
	 * m.get("a") == 1, m.get("c") == 2, m.get("d") == 2 and m.get("b") == null.
	 * 
	 * @param lst
	 *            a list of data items of type T
	 * @return a map of type Map<T, Integer>
	 */
	public static <T> Map<T, Integer> histogram(List<T> lst) {
		// put your code here!
		Map<T, Integer> map = new HashMap<T, Integer>();
		for(T t : lst) {
			if(map.get(t) == null) map.put(t, 0);
			map.put(t, map.get(t)+1);
		}
		return map;
	}
	
		

	/**
	 * Given a set s of type T, find the set of all possible subsets of s. For
	 * instance, if s = new HashSet(Arrays.asList(2,3)), then the result should
	 * equal to {{}, {2},{3},{2,3}}.
	 * 
	 * @param s
	 *            a set
	 * @return the set of all subsets of s.
	 */
	public static <T> Set<Set<T>> powerset(Set<T> s) {
		// put your code here!
		Set<Set<T>> sets = new HashSet<Set<T>>();
		if(s.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		List<T> list = new ArrayList<T>(s);
		T front = list.get(0);
		Set<T> back = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerset(back)) {
	        Set<T> newSet = new HashSet<T>();
	        newSet.add(front);
	        newSet.addAll(set);
	        sets.add(newSet);
	        sets.add(set);
	    }       
	    return sets;
	}
	
	
	/**
	 * Given a set s of positive Integers, find a subset s2 of s such that the summation of elements of s2
	 * is equal to an int y. If there is no such solution, return null. 
	 * For instance, if s = new HashSet(Arrays.asList(2,3,5,6)) and y = 8, then the result can be
	 * {3,5} or {2,6}.
	 * 
	 * @param s a set of positive integers
	 * @param y a positive int.
	 * @return a subset of s whose sum is equal to y or null if there is no such solution.
	 */
	public static Set<Integer> findAnIntegerSum(Set<Integer> s, int y) {
		// put your code here!
		Set<Set<Integer>> powersets = powerset(s);
		for(Set<Integer> set : powersets) {
			List<Integer> list = new ArrayList<Integer>(set);
			int sum = 0;
			for(int i : list) {
				sum += i;
			}
			if(sum == y) return set;
		}
		return null;
	}
	
	/**
	 * Given a set s of positive Integers, find all subsets s2 of s such that the summation of elements of s2
	 * is equal to an int y. If there is no such solution, return {}. 
	 * For instance, if s = new HashSet(Arrays.asList(2,3,5,6)) and y = 8, then the result can be
	 * {{2,6},{3,5}}.
	 * 
	 * @param s a set of positive integers
	 * @param y a positive int.
	 * @return the set of all subsets of s whose sum is equal to y or {} if there is no such solution.
	 */
	public static Set<Set<Integer>> findAllIntegerSums(Set<Integer> s, int y) {
		// put your code here!
		Set<Set<Integer>> powersets = powerset(s);
		Set<Set<Integer>> allsets = new HashSet<Set<Integer>>();
		for(Set<Integer> set : powersets) {
			List<Integer> list = new ArrayList<Integer>(set);
			int sum = 0;
			for(int i : list) {
				sum += i;
			}
			if(sum == y) allsets.add(set);
		}
		return allsets;

	}

	

	public static void main(String[] args) {

		mainTest(args) ; // uncomment this line to test your program with
		// given cases.

		// add more tests your self!
		System.out.printf("%s\n", f(1000));
	}

	public static void mainTest(String[] args) {

		
		List<Integer> lst01 = seq(4, 20, 3) ;
		
		// The result should be [4,7,10,13,16,19]
	    System.out.println(lst01);
	    
	    List<Integer> lst02 = seq(31, 6, -5);
  	    // The result should be [31,26,21,16,11,6]
	    System.out.println(lst02);
		
		
		Set<String> s1 = new HashSet<>(Arrays.asList("d", "c", "a", "c", "d"));

		// The result should be [[], [a], [c], [d], [a, c], [a, d], [c, d], [a,
		// c, d]]
		System.out.println(powerset(s1));

		Set<Integer> s2 = new TreeSet<>(Arrays.asList(4, 1, 7, 9));

		// The result should be :
		// [[], [1], [4], [1, 4], [7], [1, 7], [9], [1, 9], [4, 7], [1, 4, 7],
		// [4, 9], [1, 4, 9], [7, 9], [1, 7, 9], [4, 7, 9], [1, 4, 7, 9]] or
		// [[], [9], [7], [7, 9], [4], [4, 9], [4, 7], [4, 7, 9], [1], [1, 9],
		// [1, 7], [1, 7, 9], [1, 4], [1, 4, 9], [1, 4, 7], [1, 4, 7, 9]]
		// or their equivalents

		System.out.println(powerset(s2));

		List<String> lst1 = Arrays.asList("d", "c", "a", "c", "d");
		Map<String, Integer> h = histogram(lst1);
		System.out.println(h);

		List<Integer> lst2 = Arrays.asList(5, 7, 1, 1, 4, 6, 5, 5, 7, 3, 2, 2,
				2);
		Map<Integer, Integer> h2 = histogram(lst2);
		System.out.println(h2);
		// mainTest(args) ;
		
		

		// [5,3,7,2,1,9]
		List<Integer> lst3 = Arrays.asList(5, 3, 7, 1, 2, 9);

		List<Integer> r3 = nfSublist(lst3);

		// expected result is [5,7,2, 9].
		System.out.println("r3:" + r3);

		// [1,3,5,7,9,11,12,10,8,6,4,2]
		List<Integer> lst4 = Arrays.asList(1,3,5,7,9,11,12,10,8,6,4,2);

		List<Integer> r4 = nfSublist(lst4);
		
		

		// The result should be [7,9,11,12,10,8]
		System.out.println("r4:" + r4);
		

		
		Set<Integer> s3 = new TreeSet<>(Arrays.asList(1,3,5,7,9,11)) ;
		
		int y = 14 ;
		
		Set<Integer> sol1= findAnIntegerSum(s3, y);	
		
		// result : {3,11} or {5,9}  
		System.out.println("The sum of " + sol1 +  " is " + y + "?" ) ;
		
		
		Set<Set<Integer>> sol2= findAllIntegerSums(s3, y);	
		
		
		// result: {{3,11},{5,9}}
		System.out.println("Every and only set in " + sol2 +  " has sum " + y + "?" ) ;		

	}

}
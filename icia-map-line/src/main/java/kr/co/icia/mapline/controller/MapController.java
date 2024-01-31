package kr.co.icia.mapline.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.icia.mapline.util.KakaoApiUtil;
import kr.co.icia.mapline.util.KakaoApiUtil.Point;

@Controller
public class MapController {
	/**
	 * 키워드를 통해 마커 표시하고 선으로 잇기
	 * 
	 * @param x       중심지 x좌표
	 * @param y       중심지 y좌표
	 * @param keyword 검색 키워드
	 * @param model   html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/keyword/path") // url : /map/keyword/path
	public String getMapKeywordPath(//
			@RequestParam(required = false) Double x, //
			@RequestParam(required = false) Double y, //
			@RequestParam(required = false) String keyword, //
			Model model) throws IOException, InterruptedException {
		if (x != null && y != null && keyword != null) {
			List<Point> keywordPointList = KakaoApiUtil.getPointByKeyword(keyword, new Point(x, y));
			String keywordPointListJson = new ObjectMapper().writer().writeValueAsString(keywordPointList);
			model.addAttribute("keywordPointList", keywordPointListJson);

			List<Point> pathPointList = new ArrayList<>();
			for (int i = 1; i < keywordPointList.size(); i++) {
				Point prevPoint = keywordPointList.get(i - 1);
				Point nextPoint = keywordPointList.get(i);
				pathPointList.addAll(KakaoApiUtil.getVehiclePaths(prevPoint, nextPoint));
			}
			String pathPointListJson = new ObjectMapper().writer().writeValueAsString(pathPointList);
			model.addAttribute("pathPointList", pathPointListJson);
		}
		return "map/keyword-path";
	}

	/**
	 * 키워드를 통해 마커 표시
	 * 
	 * @param x       중심지 x좌표
	 * @param y       중심지 y좌표
	 * @param keyword 검색 키워드
	 * @param model   html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/keyword") // url : /map/keyword
	public String getMapKeyword(//
			@RequestParam(required = false) Double x, //
			@RequestParam(required = false) Double y, //
			@RequestParam(required = false) String keyword, //
			Model model) throws IOException, InterruptedException {
		if (x != null && y != null && keyword != null) {
			List<Point> pointList = KakaoApiUtil.getPointByKeyword(keyword, new Point(x, y));
			String pointListJson = new ObjectMapper().writer().writeValueAsString(pointList);
			model.addAttribute("pointList", pointListJson);
		}
		return "map/keyword";
	}

	/**
	 * 자동차 이동 경로 그리기
	 * 
	 * @param fromAddress 출발지 주소정보
	 * @param toAddress   목적지 주소정보
	 * @param model       html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/paths") // url : /map/paths
	public String getMapPaths(@RequestParam(required = false) String fromAddress, //
			@RequestParam(required = false) String toAddress, //
			Model model) throws IOException, InterruptedException {
		Point fromPoint = null;
		Point toPoint = null;
		if (fromAddress != null && !fromAddress.isEmpty()) {
			fromPoint = KakaoApiUtil.getPointByAddress(fromAddress);
			model.addAttribute("fromPoint", fromPoint);
		}
		if (toAddress != null && !toAddress.isEmpty()) {
			toPoint = KakaoApiUtil.getPointByAddress(toAddress);
			model.addAttribute("toPoint", toPoint);
		}

		if (fromPoint != null && toPoint != null) {
			List<Point> pointList = KakaoApiUtil.getVehiclePaths(fromPoint, toPoint);
			String pointListJson = new ObjectMapper().writer().writeValueAsString(pointList);
			model.addAttribute("pointList", pointListJson);
		}
		return "map/paths";
	}

	/**
	 * 주소를 좌표로 변환
	 * 
	 * @param address 주소정보
	 * @param model   html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/address/point") // url : /map/address/point
	public String getMapAddressPoint(@RequestParam(required = false) String address, Model model)
			throws IOException, InterruptedException {
		if (address != null && !address.isEmpty()) {
			Point point = KakaoApiUtil.getPointByAddress(address);
			model.addAttribute("point", point);
		}
		return "map/address_point";
	}

	/**
	 * 출발지와 목적지를 지도상에 표시하기
	 * 
	 * @param fromAddress 출발지 주소정보
	 * @param toAddress   목적지 주소정보
	 * @param model       html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/marker") // url : /map/marker
	public String getMapMarker(@RequestParam(required = false) String fromAddress, //
			@RequestParam(required = false) String toAddress, //
			Model model) throws IOException, InterruptedException {
		if (fromAddress != null && !fromAddress.isEmpty()) {
			Point fromPoint = KakaoApiUtil.getPointByAddress(fromAddress);
			model.addAttribute("fromPoint", fromPoint);
		}
		if (toAddress != null && !toAddress.isEmpty()) {
			Point toPoint = KakaoApiUtil.getPointByAddress(toAddress);
			model.addAttribute("toPoint", toPoint);
		}
		return "map/marker";
	}

}
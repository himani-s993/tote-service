package com.demo.tote.service.impl;

import com.demo.tote.dto.Bet;
import com.demo.tote.dto.Result;
import com.demo.tote.dto.ToteBetRequest;
import com.demo.tote.service.ToteService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToteServiceImpl implements ToteService {

    private static final int POSITION = 0;
    private static final int SELECTION = 1;
    private static final int STAKE = 2;
    final DecimalFormat decimalFormat = new DecimalFormat("##.00");

    @Override
    public List<String> getResultDividends(ToteBetRequest request) {
        List<Bet> betList = this.buildBetList(request.getBets());
        Result resultRunner = this.buildResult(request.getRaceResult());
        List<Bet> winBetList = new ArrayList<>();
        List<Bet> placeBetList = new ArrayList<>();
        List<Bet> exactaBetList = new ArrayList<>();
        List<Bet> quinellaBetList = new ArrayList<>();
        List<String> dividendList = new ArrayList<>();

        for (Bet bet : betList) {
            String runnerSelection = bet.getSelection();
            boolean winningHorseSelected;

            if (isWinOrPlaceBet(runnerSelection)) {
                winningHorseSelected = isWinningHorseSelected(runnerSelection, resultRunner);
            } else {
                String selection[] = runnerSelection.split(",");
                if (bet.getProduct().equals("E")) {
                    winningHorseSelected = Integer.valueOf(selection[0]) == resultRunner.getFirstPosition() &&
                            Integer.valueOf(selection[1]) == resultRunner.getSecondPosition();
                } else {
                    winningHorseSelected = Integer.valueOf(selection[0]) == resultRunner.getFirstPosition() &&
                            Integer.valueOf(selection[1]) == resultRunner.getSecondPosition() ||
                            Integer.valueOf(selection[1]) == resultRunner.getFirstPosition() &&
                                    Integer.valueOf(selection[0]) == resultRunner.getSecondPosition();
                }
            }

            //TODO: Refactor this
            if (winningHorseSelected) {
                switch (bet.getProduct()) {
                    case "W":
                        winBetList.add(bet);
                        break;
                    case "P":
                        placeBetList.add(bet);
                        break;
                    case "E":
                        exactaBetList.add(bet);
                        break;
                    case "Q":
                        quinellaBetList.add(bet);
                        break;
                }
            }
        }

        // calculate payouts
        if (!winBetList.isEmpty()) {
            dividendList.addAll(calculateDividendsForWin(winBetList, betList));
        }

        if (!placeBetList.isEmpty()) {
            dividendList.addAll(calculateDividendsForPlace(placeBetList, betList, resultRunner));
        }

        if (!exactaBetList.isEmpty()) {
            dividendList.addAll(calculateDividendsForExacta(exactaBetList, betList));
        }

        if (!quinellaBetList.isEmpty()) {
            dividendList.addAll(calculateDividendsForQuinella(quinellaBetList, betList));
        }

        return dividendList;
    }

    private List<String> calculateDividendsForWin(List<Bet> winBetList, List<Bet> betList) {
        double totalWinPoolAmt = getTotalPoolSpecificAmount("W", betList);
        double totalWinPoolAmtAfterCommission = totalWinPoolAmt - totalWinPoolAmt * 0.15;
        double totalWinnerBetAmt = winBetList.stream().mapToDouble(Bet::getStake).sum();
        double proportionForWinStake = totalWinPoolAmtAfterCommission/totalWinnerBetAmt;
        List<String> dividendList = new ArrayList<>();
        dividendList.add("Win - Runner " + winBetList.get(0).getSelection() + " - " + decimalFormat.format(proportionForWinStake));
        return dividendList;
    }

    private List<String> calculateDividendsForPlace(List<Bet> placeBetList, List<Bet> betList, Result resultRunner) {
        double totalPlacePoolAmt = getTotalPoolSpecificAmount("P", betList);
        double totalPlacePoolAmtAfterCommission = (totalPlacePoolAmt - totalPlacePoolAmt * 0.12) / 3;
        double totalFirstPlacePoolAmt = placeBetList.stream()
                .filter(bet -> Integer.valueOf(bet.getSelection()) == resultRunner.getFirstPosition())
                .mapToDouble(Bet::getStake).sum();
        double totalSecondPlacePoolAmt = placeBetList.stream()
                .filter(bet -> Integer.valueOf(bet.getSelection()) == resultRunner.getSecondPosition())
                .mapToDouble(Bet::getStake).sum();
        double totalThirdPlacePoolAmt = placeBetList.stream()
                .filter(bet -> Integer.valueOf(bet.getSelection()) == resultRunner.getThirdPosition())
                .mapToDouble(Bet::getStake).sum();
        List<String> dividendList = new ArrayList<>();

        if (totalFirstPlacePoolAmt > 0) {
            dividendList.add("Place - Runner " + resultRunner.getFirstPosition() +
                    " - " + decimalFormat.format(totalPlacePoolAmtAfterCommission / totalFirstPlacePoolAmt));
        }
        if (totalSecondPlacePoolAmt > 0) {
            dividendList.add("Place - Runner " + resultRunner.getSecondPosition() +
                    " - " + decimalFormat.format(totalPlacePoolAmtAfterCommission / totalSecondPlacePoolAmt));
        }

        if (totalThirdPlacePoolAmt > 0) {
            dividendList.add("Place - Runner " + resultRunner.getThirdPosition() +
                    " - " + decimalFormat.format(totalPlacePoolAmtAfterCommission / totalThirdPlacePoolAmt));
        }
        return dividendList;
    }

    private List<String> calculateDividendsForExacta(List<Bet> exactaBetList, List<Bet> betList) {
        double totalExactaPoolAmt = getTotalPoolSpecificAmount("E", betList);
        double totalPayoutAmt = totalExactaPoolAmt - totalExactaPoolAmt * 0.18;
        List<String> dividendList = new ArrayList<>();
        exactaBetList.stream().forEach(winBet -> {
            double proportion = (winBet.getStake() / totalExactaPoolAmt) * totalPayoutAmt;
            dividendList.add("Exacta - Runner " + winBet.getSelection() + " - " + proportion);
        });
        return dividendList;
    }

    private List<String> calculateDividendsForQuinella(List<Bet> quinellaBetList, List<Bet> betList) {
        double totalQuinellaPoolAmt = getTotalPoolSpecificAmount("Q", betList);
        double totalPayoutAmt = totalQuinellaPoolAmt - totalQuinellaPoolAmt * 0.18;
        List<String> dividendList = new ArrayList<>();
        quinellaBetList.stream().forEach(winBet -> {
            double proportion = (winBet.getStake() / totalQuinellaPoolAmt) * totalPayoutAmt;
            dividendList.add("Quinella - Runner " + winBet.getSelection() + " - " + proportion);
        });
        return dividendList;
    }

    private boolean isWinOrPlaceBet(String selection) {
        return !selection.contains(",");
    }

    private double getTotalPoolSpecificAmount(String product, List<Bet> betList) {
        return betList.stream()
                .filter(bet -> bet.getProduct().equals(product))
                .mapToDouble(Bet::getStake).sum();
    }

    private boolean isWinningHorseSelected(String inputSelection, Result resultRunner) {
        int runnerSelection = Integer.parseInt(inputSelection);
        return runnerSelection == resultRunner.getFirstPosition() ||
                runnerSelection == resultRunner.getSecondPosition() ||
                runnerSelection == resultRunner.getThirdPosition();
    }

    private List<Bet> buildBetList(List<String> bets) {
        return bets.stream().map(str -> {
            String betArray[] = str.split(":");
            Bet bet = new Bet();
            bet.setProduct(betArray[POSITION]);
            bet.setSelection((betArray[SELECTION]));
            bet.setStake(Double.valueOf(betArray[STAKE]));
            return bet;
        }).collect(Collectors.toList());
    }

    private Result buildResult(String results) {
        String raceResult[] = results.split(":");
        Result result = new Result();
        result.setFirstPosition(Integer.valueOf(raceResult[1]));
        result.setSecondPosition(Integer.valueOf(raceResult[2]));
        result.setThirdPosition(Integer.valueOf(raceResult[3]));
        return result;
    }
}

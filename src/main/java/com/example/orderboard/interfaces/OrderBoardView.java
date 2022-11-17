package com.example.orderboard.interfaces;

import com.example.orderboard.domain.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Data
class OrderBoardView {

    private final DataContainer data = new DataContainer(new HashMap<>());

    OrderBoardView(List<Order> orders) {
        orders.stream()
                .map(BoardViewItem::from)
                .forEach(data::addBoardViewItem);
    }

    @Getter
    @AllArgsConstructor
    static class BoardViewItem implements Comparable<BoardViewItem> {

        @JsonIgnore
        private final PricePerUnit pricePerUnit;
        @JsonIgnore
        private final Quantity.Unit unit;
        @JsonIgnore
        private final OrderType type;
        @JsonIgnore
        private Quantity quantity;

        static BoardViewItem from(Order order) {
            return new BoardViewItem(order.pricePerUnit(), order.quantity().unit(), order.type(), order.quantity());
        }

        @JsonProperty("pricePerUnit")
        BigDecimal getPrice() {
            return this.pricePerUnit.amount();
        }

        @JsonProperty("quantity")
        BigInteger getQuantity() {
            return this.quantity.amount();
        }

        boolean isMergePossible(BoardViewItem otherItem) {
            return isTheSamePricePerUnit(otherItem)
                    && isTheSameUnit(otherItem);
        }

        void merge(BoardViewItem item) {
            this.quantity = new Quantity(this.quantity.amount().add(item.quantity.amount()), item.unit);
        }

        private boolean isTheSameUnit(BoardViewItem otherItem) {
            return this.unit == otherItem.unit;
        }

        private boolean isTheSamePricePerUnit(BoardViewItem otherItem) {
            return this.pricePerUnit.currencyCode().equals(otherItem.pricePerUnit.currencyCode())
                    && this.pricePerUnit.amount().compareTo(otherItem.pricePerUnit.amount()) == 0;
        }

        @Override
        public int compareTo(BoardViewItem other) {
            int isCurrentHigher = other.getPricePerUnit().amount().compareTo(pricePerUnit.amount());
            int isSell = this.getType() == OrderType.BUY ? 1 : -1;
            return isCurrentHigher * isSell;
        }
    }

    record DataContainer(@JsonIgnore Map<Metadata, TreeSet<BoardViewItem>> items) {

        @JsonProperty("byMetadata")
        List<MetadataWithItems> getView() {
            return this.items.entrySet().stream()
                    .map(entry -> new MetadataWithItems(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }

        void addBoardViewItem(BoardViewItem boardViewItem) {
            Metadata metadata = new Metadata(
                    boardViewItem.type,
                    boardViewItem.unit,
                    boardViewItem.pricePerUnit.currencyCode());

            var items = this.items.computeIfAbsent(metadata, k -> new TreeSet<>());
            items.stream()
                    .filter(baseOrder -> baseOrder.isMergePossible(boardViewItem))
                    .findFirst()
                    .ifPresentOrElse(
                            baseOrder -> baseOrder.merge(boardViewItem),
                            () -> items.add(boardViewItem)
                    );
        }

        record MetadataWithItems(@JsonIgnore Metadata metadata, TreeSet<BoardViewItem> items) {
            @JsonProperty("orderType")
            String getOrderType() {
                return this.metadata.orderType.name();
            }

            @JsonProperty("unit")
            String getUnit() {
                return this.metadata.unit.name();
            }

            @JsonProperty("currencyCode")
            String getCurrencyCode() {
                return this.metadata.currencyCode.code();
            }
        }

        record Metadata(OrderType orderType, Quantity.Unit unit, PricePerUnit.CurrencyCode currencyCode) {
        }
    }
}
